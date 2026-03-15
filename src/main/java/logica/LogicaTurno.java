package logica;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.clases.Turno;
import logica.clases.Paciente;
import logica.clases.TipoConsulta;
import logica.clases.Turno.Estado;
import logica.exceptions.CampoInvalido;
import logica.exceptions.EstadoInvalido;
import logica.exceptions.TurnoReprogramarPasado;
import persistencia.exceptions.ProblemaPersistencia;

public class LogicaTurno {

    private ControladoraLogica controladoraLogica;

    public LogicaTurno(ControladoraLogica controladoraLogica) {
        this.controladoraLogica = controladoraLogica;
    }

    public List<Turno> traerTodos() {
        return controladoraLogica.traerTurnos();
    }

    public List<Turno> traerTurnosHoy() {
        List<Turno> listaTurnos = controladoraLogica.traerTurnos();

        //Mantener los turnos de hoy y que estén confirmados, pendientes o atendidos
        Calendar calendarioHoy = Calendar.getInstance();
        calendarioHoy.setTime(new Date());
        Calendar calendarioFechaTurno = Calendar.getInstance();

        Iterator<Turno> iteradorListaTurnos = listaTurnos.iterator();
        while (iteradorListaTurnos.hasNext()) {
            Turno proximoTurno = iteradorListaTurnos.next();
            Estado estadoTurno = proximoTurno.getEstado();
            calendarioFechaTurno.setTime(proximoTurno.getHoraInicio());
            if (!(calendarioFechaTurno.get(Calendar.YEAR) == calendarioHoy.get(Calendar.YEAR)
                    && calendarioFechaTurno.get(Calendar.MONTH) == calendarioHoy.get(Calendar.MONTH)
                    && calendarioFechaTurno.get(Calendar.DATE) == calendarioHoy.get(Calendar.DATE))
                    || !(estadoTurno == Turno.Estado.CONFIRMADO || estadoTurno == Turno.Estado.PENDIENTE)) {
                iteradorListaTurnos.remove();
            }
        }

        //Ordenar por hora de incio de forma ascendente
        Collections.sort(listaTurnos, new Comparator<Turno>() {
            @Override
            public int compare(Turno turno1, Turno turno2) {
                return turno1.getHoraInicio().compareTo(turno2.getHoraInicio());
            }
        }
        );

        return listaTurnos;
    }

    //Recibe la lista de turnos ya ordenada
    public Paciente traerProximoPaciente(List<Turno> turnosHoy) {
        //Eliminar turnos que no estén confirmados y que ya han pasado hasta el momento
        Date horaActual = new Date();

        Iterator<Turno> iteradorTurnosHoy = turnosHoy.iterator();

        while (iteradorTurnosHoy.hasNext()) {
            Turno proximoTurno = iteradorTurnosHoy.next();

            if ((proximoTurno.getEstado() != Turno.Estado.CONFIRMADO) && (proximoTurno.getHoraFinal().before(horaActual))) {
                iteradorTurnosHoy.remove();
            }
        }

        //Buscar al paciente del próximo turno
        Paciente proximoPaciente;

        if (!turnosHoy.isEmpty()) {
            proximoPaciente = turnosHoy.get(0).getPaciente();
        } else {
            proximoPaciente = null;
        }

        return proximoPaciente; //Esto puede devolver null, aclararlo en la IGU
    }

    public void crearNuevo(Turno turno) {
        try {
            verificarCampos(turno);
        } catch (CampoInvalido e) {
            e.printStackTrace();
            throw e;
        }
        controladoraLogica.crearTurno(turno);
    }

    public List<Turno> buscarPorFecha(Date fechaBuscada) {
        List<Turno> listaTurnos = controladoraLogica.traerTurnos();

        Iterator<Turno> iteradorListaTurnos = listaTurnos.iterator();
        while (iteradorListaTurnos.hasNext()) {
            Turno proximoTurno = iteradorListaTurnos.next();
            short anio = (short) proximoTurno.getHoraInicio().getYear();
            short mes = (short) proximoTurno.getHoraInicio().getMonth();
            short dia = (short) proximoTurno.getHoraInicio().getDate();
            if (!(anio == fechaBuscada.getYear() && mes == fechaBuscada.getMonth() && dia == fechaBuscada.getDate())) {
                iteradorListaTurnos.remove();
            }
        }

        return listaTurnos;
    }

    public List<Turno> filtrarPorEstado(Estado estadoBuscado) {
        List<Turno> listaTurnos = controladoraLogica.traerTurnos();

        Iterator<Turno> iteradorListaTurnos = listaTurnos.iterator();
        while (iteradorListaTurnos.hasNext()) {
            Turno proximoTurno = iteradorListaTurnos.next();
            if (proximoTurno.getEstado() != estadoBuscado) {
                iteradorListaTurnos.remove();
            }
        }

        return listaTurnos;
    }

    public List<Turno> filtrarPorFechaEstado(Date fechaBuscada, Estado estadoBuscado) {
        List<Turno> listaTurnos = controladoraLogica.traerTurnos();

        Iterator<Turno> iteradorListaTurnos = listaTurnos.iterator();
        while (iteradorListaTurnos.hasNext()) {
            Turno proximoTurno = iteradorListaTurnos.next();
            short anio = (short) proximoTurno.getHoraInicio().getYear();
            short mes = (short) proximoTurno.getHoraInicio().getMonth();
            short dia = (short) proximoTurno.getHoraInicio().getDate();
            if (!(anio == fechaBuscada.getYear() && mes == fechaBuscada.getMonth() && dia == fechaBuscada.getDate() && proximoTurno.getEstado() == estadoBuscado)) {
                iteradorListaTurnos.remove();
            }
        }

        return listaTurnos;
    }

    public List<Turno> ordenarPorFechaCercanaLejana(List<Turno> listaTurnos) {
        //Ordenar por hora de incio de forma ascendente
        Collections.sort(listaTurnos, new Comparator<Turno>() {
            @Override
            public int compare(Turno turno1, Turno turno2) {
                return turno1.getHoraInicio().compareTo(turno2.getHoraInicio());
            }
        }
        );

        return listaTurnos;
    }

    public void confirmarTurno(long idTurno) {
        Turno turno = controladoraLogica.traerTurno(idTurno);

        if (!turno.puedeConfirmar()) {
            throw new EstadoInvalido("El turno no puede cambiar su estado a Confirmado.");
        }

        turno.setEstado(Estado.CONFIRMADO);

        try {
            controladoraLogica.editarTurno(turno);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar confirmar un turno.");
        }
    }

    public void atenderTurno(long idTurno) {
        Turno turno = controladoraLogica.traerTurno(idTurno);

        if (!turno.puedeAtender()) {
            throw new EstadoInvalido("El turno no puede cambiar su estado a Atendido.");
        }

        turno.setEstado(Estado.ATENDIDO);

        try {
            controladoraLogica.editarTurno(turno);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar atender un turno.");
        }
    }

    public void reprogramarTurno(Turno turnoReprogramado, Turno nuevoTurno) {
        if (turnoReprogramado.getHoraInicio().before(new Date())) {
            throw new TurnoReprogramarPasado("No se puede repogramar turnos cuya fecha ya ha pasado.");
        }

        try {
            verificarCampos(nuevoTurno);
        } catch (CampoInvalido e) {
            e.printStackTrace();
            throw e;
        }

        turnoReprogramado.setReprogramado(true);
        turnoReprogramado.setEstado(Estado.CANCELADO);
        try {
            controladoraLogica.editarTurno(turnoReprogramado); //¿Dará problemas si lo mando a BD antes de usar sus datos?
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar cancelar un turno para reprogramarlo.");
        }

        controladoraLogica.crearTurno(nuevoTurno);
    }

    public void cancelarTurno(long idTurno) {
        Turno turno = controladoraLogica.traerTurno(idTurno);

        if (!turno.puedeCancelar()) {
            throw new EstadoInvalido("El turno no puede cambiar su estado a Cancelado.");
        }

        turno.setEstado(Estado.CANCELADO);

        try {
            controladoraLogica.editarTurno(turno);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar cancelar un turno.");
        }
    }

    public Map<Date, Date> traerHorariosDisponibles(Date diaSeleccionado, TipoConsulta tipoConsultaSeleccionada, Turno turnoIgnorar) {

        //Guardar los turnos del día seleccionado
        Calendar calendarioDiaSeleccionado = Calendar.getInstance();
        calendarioDiaSeleccionado.setTime(diaSeleccionado);

        List<Turno> listaTurnos = controladoraLogica.traerTurnos();
        Iterator<Turno> iteradorListaTurnos = listaTurnos.iterator();

        while (iteradorListaTurnos.hasNext()) {
            Turno proximoTurno = iteradorListaTurnos.next();
            Calendar horaInicioProximoTurno = Calendar.getInstance();
            horaInicioProximoTurno.setTime(proximoTurno.getHoraInicio());

            if (!(horaInicioProximoTurno.get(Calendar.YEAR) == calendarioDiaSeleccionado.get(Calendar.YEAR)
                    && horaInicioProximoTurno.get(Calendar.MONTH) == calendarioDiaSeleccionado.get(Calendar.MONTH)
                    && horaInicioProximoTurno.get(Calendar.DATE) == calendarioDiaSeleccionado.get(Calendar.DATE)
                    && proximoTurno.getEstado() != Estado.CANCELADO)) {
                iteradorListaTurnos.remove();
            }
        }

        Map<Date, Date> horariosDisponibles = new LinkedHashMap();

        Calendar ultimaHoraJornada = Calendar.getInstance();
        ultimaHoraJornada.setTime(diaSeleccionado);
        ultimaHoraJornada.set(Calendar.HOUR_OF_DAY, 17); //Hora final de la jornada
        ultimaHoraJornada.set(Calendar.MINUTE, 0);
        ultimaHoraJornada.set(Calendar.SECOND, 0);
        ultimaHoraJornada.set(Calendar.MILLISECOND, 0);

        Calendar horaInicialNuevoTurno = Calendar.getInstance();
        horaInicialNuevoTurno.setTime(diaSeleccionado);
        horaInicialNuevoTurno.set(Calendar.HOUR_OF_DAY, 8); //Primera hora de la jornada 
        horaInicialNuevoTurno.set(Calendar.MINUTE, 0);
        horaInicialNuevoTurno.set(Calendar.SECOND, 0);
        horaInicialNuevoTurno.set(Calendar.MILLISECOND, 0);

        Calendar horaFinalNuevoTurno = Calendar.getInstance();
        horaFinalNuevoTurno.setTime(diaSeleccionado);
        horaFinalNuevoTurno.set(Calendar.HOUR_OF_DAY, 8);
        horaFinalNuevoTurno.add(Calendar.MINUTE, tipoConsultaSeleccionada.getDuracionMinutos()); //Sumar duración de la consulta
        horaFinalNuevoTurno.set(Calendar.SECOND, 0);
        horaFinalNuevoTurno.set(Calendar.MILLISECOND, 0);

        //Los horarios disponibles se manejaran en intervalos de 5 minutos para no hacerlo tan verboso en la interfaz
        int intervaloMinutos = 5;

        //Esto es para guardar la hora de inicio y finalización de los turnos en la fecha seleccionada que son Date
        Calendar turnoHoraInicio = Calendar.getInstance();
        Calendar turnoHoraFinal = Calendar.getInstance();

        //Los turnos no deben sobrepasar el horario de salida
        while (horaFinalNuevoTurno.before(ultimaHoraJornada) || horaFinalNuevoTurno.equals(ultimaHoraJornada)) {

            //Verificar que tanto la hora de inicio y finalización no se encuentren entre los intervalos de duración de los turnos del día seleccioando
            boolean horarioDisponible = true;

            for (Turno turno : listaTurnos) {

                if (turnoIgnorar != null && turnoIgnorar.equals(turno)) {
                    continue; //Salta esta iteración
                }

                turnoHoraInicio.setTime(turno.getHoraInicio());
                turnoHoraFinal.setTime(turno.getHoraFinal());
                turnoHoraInicio.set(Calendar.MILLISECOND, 0);
                turnoHoraFinal.set(Calendar.MILLISECOND, 0);

                if (horaInicialNuevoTurno.before(turnoHoraFinal) && horaFinalNuevoTurno.after(turnoHoraInicio)) {
                    horarioDisponible = false;
                    break;
                }
            }

            if (horarioDisponible) {
                horariosDisponibles.put(horaInicialNuevoTurno.getTime(), horaFinalNuevoTurno.getTime());
            }

            //Se muestran intervalos de 5 en 5
            horaInicialNuevoTurno.add(Calendar.MINUTE, intervaloMinutos);
            horaFinalNuevoTurno.add(Calendar.MINUTE, intervaloMinutos);
        }

        return horariosDisponibles;
    }
    
    public List<Turno> traerTurnosRevision() {
        List<Turno> listaTurnos = controladoraLogica.traerTurnos();

        Date fechaHoy = new Date();
        Iterator<Turno> iteradorListaTurnos = listaTurnos.iterator();
        while (iteradorListaTurnos.hasNext()) {
            Turno proximoTurno = iteradorListaTurnos.next();

            if (!(proximoTurno.getHoraInicio().before(fechaHoy) && (proximoTurno.getEstado() == Turno.Estado.CONFIRMADO || proximoTurno.getEstado() == Turno.Estado.PENDIENTE))) {
                iteradorListaTurnos.remove();
            }
        }

        return listaTurnos;
    }

    public void cambiarEstadoTurnoRevisado(long idTurnoRevisado) {
        Turno turno = controladoraLogica.traerTurno(idTurnoRevisado);

        if (turno.getEstado() == Estado.CONFIRMADO) {
            turno.setEstado(Estado.AUSENTADO);
        }

        if (turno.getEstado() == Estado.PENDIENTE) {
            turno.setEstado(Estado.CANCELADO);
        }

        try {
            controladoraLogica.editarTurno(turno);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProblemaPersistencia("Ha ocurrido un error al intentar cambiar el estado del turno revisado..");
        }
    }

    private void verificarCampos(Turno turno) {
        if (turno.getPaciente() == null) {
            throw new CampoInvalido("No se ha seleccionado un paciente para el nuevo turno.");
        }
        if (turno.getTipoConsulta() == null) {
            throw new CampoInvalido("No se ha seleccionado un tipo de consulta para el nuevo turno.");
        }

        Date fechaHoy = new Date();
        if (turno.getHoraInicio() == null || turno.getHoraInicio().before(fechaHoy)) {
            throw new CampoInvalido("El horario inicial ingresado para el nuevo turno no es válido.");
        }
        if (turno.getHoraFinal() == null || turno.getHoraFinal().before(fechaHoy) || turno.getHoraFinal().before(turno.getHoraInicio())) {
            throw new CampoInvalido("El horario final ingresado para el nuevo turno no es válido.");
        }
    }
}
