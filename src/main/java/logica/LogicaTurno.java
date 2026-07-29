package logica;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import logica.clases.Turno;
import logica.clases.Paciente;
import logica.clases.TipoConsulta;
import logica.clases.Turno.Estado;
import logica.exceptions.HorarioInvalido;
import logica.exceptions.TipoConsultaInvalido;
import persistencia.exceptions.ProblemaPersistencia;

public class LogicaTurno {
    
    private final ControladoraLogica controladoraLogica;
    
    public LogicaTurno(ControladoraLogica controladoraLogica) {
        this.controladoraLogica = controladoraLogica;
    }

    /*
    CREATE
     */
    public void crearNuevo(Date fechaHoraInicial, Date fechaHoraFinal, Paciente paciente, TipoConsulta tipoConsulta) {
        if (fechaHoraFinal.before(new Date())) {
            throw new HorarioInvalido("La fecha y hora inicial del nuevo turno no puede ser anterior a la fecha y hora actual.");
        }
        
        if (!tipoConsulta.isHabilitado()) {
            throw new TipoConsultaInvalido("El tipo de consulta del nuevo turno no debe estar deshabilitado.");
        }
        
        Turno nuevoTurno = new Turno(fechaHoraInicial, fechaHoraFinal, paciente, tipoConsulta);
        controladoraLogica.crearTurno(nuevoTurno);
    }

    /*
    READ
     */
    public List<Turno> traerTodos() {
        return controladoraLogica.traerTurnos();
    }
    
    public List<Turno> buscarPorFecha(Date fechaBuscada) {
        // Se reutiliza el Calendar para evitar crear uno por iteración
        // Este stream debe permanecer secuencial.
        Calendar calendarioFechaBuscada = Calendar.getInstance();
        calendarioFechaBuscada.setTime(fechaBuscada);
        int anioBuscado = calendarioFechaBuscada.get(Calendar.YEAR); // effectively final
        int mesBuscado = calendarioFechaBuscada.get(Calendar.MONTH);
        int diaBuscado = calendarioFechaBuscada.get(Calendar.DAY_OF_MONTH);
        
        Calendar calendarioFechaTurno = Calendar.getInstance();
        
        return controladoraLogica.traerTurnos().stream()
                .filter(turno -> {
                    calendarioFechaTurno.setTime(turno.getFechaHoraInicial()); // side effect
                    return (calendarioFechaTurno.get(Calendar.YEAR) == anioBuscado
                            && calendarioFechaTurno.get(Calendar.MONTH) == mesBuscado
                            && calendarioFechaTurno.get(Calendar.DAY_OF_MONTH) == diaBuscado);
                })
                .toList();
    }
    
    public List<Turno> ordenarPorFechaAscendente(List<Turno> listaTurnos) {
        return listaTurnos.stream()
                .sorted(Comparator.comparing(Turno::getFechaHoraInicial))
                .toList();
    }

    //Recibe la lista de turnos ya ordenada y filtrada con los turnos pendientes
    public Optional<Paciente> traerProximoPaciente(List<Turno> turnosHoy) {
        Date horaActual = new Date();
        
        return turnosHoy.stream()
                .filter(turno -> turno.getFechaHoraFinal().after(horaActual))
                .filter(turno -> turno.getPaciente() != null)
                .map(Turno::getPaciente)
                .findFirst();
    }
    
    public List<Turno> filtrarPorEstado(Estado estadoBuscado) {
        return filtrarListaPorEstado(controladoraLogica.traerTurnos(), estadoBuscado);
    }
    
    public List<Turno> filtrarPorFechaEstado(Date fechaBuscada, Turno.Estado estadoBuscado) {
        return filtrarListaPorEstado(buscarPorFecha(fechaBuscada), estadoBuscado);
    }
    
    private List<Turno> filtrarListaPorEstado(List<Turno> listaTurnos, Turno.Estado estadoBuscado) {
        return listaTurnos.stream()
                .filter(turno -> turno.getEstado() == estadoBuscado)
                .toList();
    }
    
    public List<Turno> traerTurnosHoyPendientes() {
        List<Turno> turnosHoy = buscarPorFecha(new Date());
        turnosHoy = ordenarPorFechaAscendente(turnosHoy);
        return filtrarListaPorEstado(turnosHoy, Estado.PENDIENTE);
    }
    
    public List<Turno> traerTurnosAyerPendientes() {
        Calendar calendarioAyer = Calendar.getInstance();
        calendarioAyer.setTime(new Date());
        calendarioAyer.add(Calendar.DAY_OF_MONTH, -1);
        
        List<Turno> turnosAyer = buscarPorFecha(calendarioAyer.getTime());
        return filtrarListaPorEstado(turnosAyer, Estado.PENDIENTE);
    }
    
    public Map<Date, Date> traerHorariosDisponibles(Date diaSeleccionado, TipoConsulta tipoConsultaSeleccionada, Turno turnoIgnorar) {
        
        List<Turno> turnosDiaSeleccionado = buscarPorFecha(diaSeleccionado).stream()
                .filter(
                        turno -> (turno.getEstado() == Turno.Estado.PENDIENTE)
                        && (turnoIgnorar == null || !turno.equals(turnoIgnorar)) // En caso de que se esté reprogramando un turno, no se debería considerar los horarios de este
                )
                .toList();
        
        Map<Date, Date> horariosDisponibles = new LinkedHashMap();
        
        Calendar ultimaHoraJornada = Calendar.getInstance();
        ultimaHoraJornada.setTime(diaSeleccionado);
        ultimaHoraJornada.set(Calendar.HOUR_OF_DAY, 17); //Hora final de la jornada
        ultimaHoraJornada.set(Calendar.MINUTE, 0);
        ultimaHoraJornada.set(Calendar.SECOND, 0);
        ultimaHoraJornada.set(Calendar.MILLISECOND, 0);
        
        Calendar posibleHoraInicialNuevoTurno = Calendar.getInstance();
        posibleHoraInicialNuevoTurno.setTime(diaSeleccionado);
        posibleHoraInicialNuevoTurno.set(Calendar.HOUR_OF_DAY, 8); //Primera hora de la jornada 
        posibleHoraInicialNuevoTurno.set(Calendar.MINUTE, 0);
        posibleHoraInicialNuevoTurno.set(Calendar.SECOND, 0);
        posibleHoraInicialNuevoTurno.set(Calendar.MILLISECOND, 0);
        
        Calendar posibleHoraFinalNuevoTurno = Calendar.getInstance();
        posibleHoraFinalNuevoTurno.setTime(diaSeleccionado);
        posibleHoraFinalNuevoTurno.set(Calendar.HOUR_OF_DAY, 8);
        posibleHoraFinalNuevoTurno.add(Calendar.MINUTE, tipoConsultaSeleccionada.getDuracionMinutos()); //Sumar duración de la consulta
        posibleHoraFinalNuevoTurno.set(Calendar.SECOND, 0);
        posibleHoraFinalNuevoTurno.set(Calendar.MILLISECOND, 0);

        //Los horarios disponibles se manejaran en intervalos de 5 minutos
        int intervaloMinutos = 5;

        //Esto es para guardar la hora de inicio y finalización de los turnos ya registrados en la fecha seleccionada que son Date
        Calendar horaInicialTurno = Calendar.getInstance();
        Calendar horaFinalTurno = Calendar.getInstance();

        //Esto es para que la hora de inicio del nuevo turno no sea anterior a la hora actual
        Calendar horaActual = Calendar.getInstance();
        horaActual.setTime(new Date());
        horaActual.set(Calendar.MILLISECOND, 0);

        //Los turnos no deben sobrepasar el horario de salida
        while (posibleHoraFinalNuevoTurno.before(ultimaHoraJornada) || posibleHoraFinalNuevoTurno.equals(ultimaHoraJornada)) {

            //Verificar que la hora de inicio y de finalización no se encuentren entre los intervalos de duración de los turnos ya registrados en el día seleccionado
            //Verificar que la hora de inicio del nuevo turno no sea anterior a la hora actual
            boolean horarioDisponible = true;
            
            for (Turno turno : turnosDiaSeleccionado) {
                horaInicialTurno.setTime(turno.getFechaHoraInicial());
                horaFinalTurno.setTime(turno.getFechaHoraFinal());
                horaInicialTurno.set(Calendar.MILLISECOND, 0);
                horaFinalTurno.set(Calendar.MILLISECOND, 0);
                
                if (posibleHoraInicialNuevoTurno.before(horaFinalTurno) && posibleHoraFinalNuevoTurno.after(horaInicialTurno)) {
                    horarioDisponible = false;
                    break;
                }
            }
            
            if (posibleHoraInicialNuevoTurno.before(horaActual)) {
                horarioDisponible = false;
            }
            
            if (horarioDisponible) {
                horariosDisponibles.put(posibleHoraInicialNuevoTurno.getTime(), posibleHoraFinalNuevoTurno.getTime());
            }

            //Se muestran intervalos de 5 en 5
            posibleHoraInicialNuevoTurno.add(Calendar.MINUTE, intervaloMinutos);
            posibleHoraFinalNuevoTurno.add(Calendar.MINUTE, intervaloMinutos);
        }
        
        return horariosDisponibles;
    }

    /*
    UPDATE
     */
    public void atenderTurno(Turno turno) {
        turno.setEstado(Estado.ATENDIDO);
        
        try {
            controladoraLogica.editarTurno(turno);
        } catch (Exception e) {
            throw new ProblemaPersistencia();
        }
    }
    
    public void reprogramarTurno(Turno turnoReprogramar, Date fechaHoraInicial, Date fechaHoraFinal) {
        if (turnoReprogramar.getFechaHoraFinal().before(new Date())) {
            throw new HorarioInvalido("Para reprogramar un turno su fecha y hora final no debe ser anterior a la fecha y hora actual.");
        }

        // Importante el orden de este proceso. Ya que no se pueden marcar como 'reprogramado' cursos que no hayan sido cancelados previamente
        turnoReprogramar.setEstado(Estado.CANCELADO);
        turnoReprogramar.setReprogramado(true);
        
        Turno nuevoTurno = new Turno(fechaHoraInicial, fechaHoraFinal, turnoReprogramar.getPaciente(), turnoReprogramar.getTipoConsulta());

        // Esto puede generar problemas de inconsistencia de datos en la BD. Se solucionará en el futuro
        try {
            controladoraLogica.editarTurno(turnoReprogramar);
        } catch (Exception e) {
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar cancelar un turno para reprogramarlo.");
        }
        
        controladoraLogica.crearTurno(nuevoTurno);
    }
    
    public void cancelarTurno(Turno turno) {
        turno.setEstado(Estado.CANCELADO);
        
        try {
            controladoraLogica.editarTurno(turno);
        } catch (Exception e) {
            throw new ProblemaPersistencia();
        }
    }
    
    public void ausentarTurno(Turno turno) {
        turno.setEstado(Estado.AUSENTADO);
        
        try {
            controladoraLogica.editarTurno(turno);
        } catch (Exception e) {
            throw new ProblemaPersistencia();
        }
    }
}
