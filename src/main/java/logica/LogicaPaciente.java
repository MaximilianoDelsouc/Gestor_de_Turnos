package logica;

import java.util.Iterator;
import java.util.List;
import logica.clases.Paciente;
import logica.clases.Turno;
import logica.clases.Turno.Estado;
import logica.exceptions.CampoInvalido;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.ProblemaPersistencia;

public class LogicaPaciente {

    private ControladoraLogica controladoraLogica;

    public LogicaPaciente(ControladoraLogica controladoraLogica) {
        this.controladoraLogica = controladoraLogica;
    }

    public void crearNuevo(Paciente nuevoPaciente) {
        try {
            verificarCampos(nuevoPaciente);
        } catch (CampoInvalido e) {
            e.printStackTrace();
            throw e;
        }

        controladoraLogica.crearPaciente(nuevoPaciente);
    }

    public List<Paciente> traerTodos() {
        return controladoraLogica.traerPacientes();
    }

    public List<Paciente> buscar(String nombreBuscado, String apellidoBuscado, String dniBuscado) {

        if ((nombreBuscado.isBlank() || nombreBuscado == null) && (apellidoBuscado.isBlank() || apellidoBuscado == null) && (dniBuscado.isBlank() || dniBuscado == null)) {
            return traerTodos();
        }

        if ((apellidoBuscado.isBlank() || apellidoBuscado == null) && (dniBuscado.isBlank() || dniBuscado == null)) {
            return buscarPorNombre(nombreBuscado);
        }
        if ((nombreBuscado.isBlank() || nombreBuscado == null) && (dniBuscado.isBlank() || dniBuscado == null)) {
            return buscarPorApellido(apellidoBuscado);
        }
        if ((nombreBuscado.isBlank() || nombreBuscado == null) && (apellidoBuscado.isBlank() || apellidoBuscado == null)) {
            return buscarPorDni(dniBuscado);
        }
        if (dniBuscado.isBlank() || dniBuscado == null) {
            return buscarPorNombreApellido(nombreBuscado, apellidoBuscado);
        }
        if (apellidoBuscado.isBlank() || apellidoBuscado == null) {
            return buscarPorNombreDni(nombreBuscado, dniBuscado);
        }
        if (nombreBuscado.isBlank() || nombreBuscado == null) {
            return buscarPorApellidoDni(apellidoBuscado, dniBuscado);
        }

        return buscarPorNombreApellidoDni(nombreBuscado, apellidoBuscado, dniBuscado);
    }

    private List<Paciente> buscarPorNombre(String nombreBuscado) {
        List<Paciente> todosPacientes = controladoraLogica.traerPacientes();

        Iterator<Paciente> iteradorTodosPaciente = todosPacientes.iterator();
        while (iteradorTodosPaciente.hasNext()) {
            Paciente proximoPaciente = iteradorTodosPaciente.next();
            if (!proximoPaciente.getNombre().equalsIgnoreCase(nombreBuscado)) {
                iteradorTodosPaciente.remove();
            }
        }

        return todosPacientes;
    }

    private List<Paciente> buscarPorApellido(String apellidoBuscado) {
        List<Paciente> todosPacientes = controladoraLogica.traerPacientes();

        Iterator<Paciente> iteradorTodosPaciente = todosPacientes.iterator();
        while (iteradorTodosPaciente.hasNext()) {
            Paciente proximoPaciente = iteradorTodosPaciente.next();
            if (!proximoPaciente.getApellido().equalsIgnoreCase(apellidoBuscado)) {
                iteradorTodosPaciente.remove();
            }
        }

        return todosPacientes;
    }

    private List<Paciente> buscarPorDni(String dniBuscado) {
        List<Paciente> todosPacientes = controladoraLogica.traerPacientes();

        Iterator<Paciente> iteradorTodosPaciente = todosPacientes.iterator();
        while (iteradorTodosPaciente.hasNext()) {
            Paciente proximoPaciente = iteradorTodosPaciente.next();
            if (!proximoPaciente.getDni().equalsIgnoreCase(dniBuscado)) {
                iteradorTodosPaciente.remove();
            }
        }

        return todosPacientes;
    }

    private List<Paciente> buscarPorNombreApellido(String nombreBuscado, String apellidoBuscado) {
        List<Paciente> todosPacientes = controladoraLogica.traerPacientes();

        Iterator<Paciente> iteradorTodosPaciente = todosPacientes.iterator();
        while (iteradorTodosPaciente.hasNext()) {
            Paciente proximoPaciente = iteradorTodosPaciente.next();
            if (!(proximoPaciente.getNombre().equalsIgnoreCase(nombreBuscado) && proximoPaciente.getApellido().equalsIgnoreCase(apellidoBuscado))) {
                iteradorTodosPaciente.remove();
            }
        }

        return todosPacientes;
    }

    private List<Paciente> buscarPorNombreDni(String nombreBuscado, String dniBuscado) {
        List<Paciente> todosPacientes = controladoraLogica.traerPacientes();

        Iterator<Paciente> iteradorTodosPaciente = todosPacientes.iterator();
        while (iteradorTodosPaciente.hasNext()) {
            Paciente proximoPaciente = iteradorTodosPaciente.next();
            if (!(proximoPaciente.getNombre().equalsIgnoreCase(nombreBuscado) && proximoPaciente.getDni().equalsIgnoreCase(dniBuscado))) {
                iteradorTodosPaciente.remove();
            }
        }

        return todosPacientes;
    }

    private List<Paciente> buscarPorApellidoDni(String apellidoBuscado, String dniBuscado) {
        List<Paciente> todosPacientes = controladoraLogica.traerPacientes();

        Iterator<Paciente> iteradorTodosPaciente = todosPacientes.iterator();
        while (iteradorTodosPaciente.hasNext()) {
            Paciente proximoPaciente = iteradorTodosPaciente.next();
            if (!(proximoPaciente.getApellido().equalsIgnoreCase(apellidoBuscado) && proximoPaciente.getDni().equalsIgnoreCase(dniBuscado))) {
                iteradorTodosPaciente.remove();
            }
        }

        return todosPacientes;
    }

    private List<Paciente> buscarPorNombreApellidoDni(String nombreBuscado, String apellidoBuscado, String dniBuscado) {
        List<Paciente> todosPacientes = controladoraLogica.traerPacientes();

        Iterator<Paciente> iteradorTodosPaciente = todosPacientes.iterator();
        while (iteradorTodosPaciente.hasNext()) {
            Paciente proximoPaciente = iteradorTodosPaciente.next();
            if (!(proximoPaciente.getNombre().equalsIgnoreCase(nombreBuscado) && proximoPaciente.getApellido().equalsIgnoreCase(apellidoBuscado)
                    && proximoPaciente.getDni().equals(dniBuscado))) {
                iteradorTodosPaciente.remove();
            }
        }

        return todosPacientes;
    }

    public Paciente traerSeleccionado(long idPaciente) {
        return controladoraLogica.traerPaciente(idPaciente);
    }

    public void editarDatos(Paciente paciente) {
        try {
            verificarCampos(paciente);
        } catch (CampoInvalido e) {
            e.printStackTrace();
            throw e;
        }

        try {
            controladoraLogica.editarPaciente(paciente);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar editar un paciente.");
        }
    }

    public void eliminar(long idPaciente) {
        try {
            controladoraLogica.eliminarPaciente(idPaciente);
        } catch (NonexistentEntityException ex) {
            ex.printStackTrace();
            throw new ProblemaPersistencia("Ha ocurrido un problema al intentar eliminar un paciente.");
        }
    }

    public List<Turno> traerHistorialCompleto(long idPaciente) {
        Paciente pacienteHistorial = controladoraLogica.traerPaciente(idPaciente);
        return pacienteHistorial.getTurnos();
    }

    public List<Turno> traerHistorialTurnosAtendidos(long idPaciente) {
        Paciente pacienteHistorial = controladoraLogica.traerPaciente(idPaciente);
        List<Turno> historial = pacienteHistorial.getTurnos();

        Iterator<Turno> iteradorHistorialCompleto = historial.iterator();
        while (iteradorHistorialCompleto.hasNext()) {
            Turno proximoTurno = iteradorHistorialCompleto.next();
            if (proximoTurno.getEstado() != Turno.Estado.ATENDIDO) {
                iteradorHistorialCompleto.remove();
            }
        }

        return historial;
    }

    public List<Turno> traerHistorialTurnosCanceladosAusentados(long idPaciente) {
        Paciente pacienteHistorial = controladoraLogica.traerPaciente(idPaciente);
        List<Turno> historial = pacienteHistorial.getTurnos();

        Iterator<Turno> iteradorHistorialCompleto = historial.iterator();
        while (iteradorHistorialCompleto.hasNext()) {
            Turno proximoTurno = iteradorHistorialCompleto.next();
            Estado estadoTurno = proximoTurno.getEstado();
            if (estadoTurno != Turno.Estado.CANCELADO && estadoTurno != Turno.Estado.AUSENTADO) {
                iteradorHistorialCompleto.remove();
            }
        }

        return historial;
    }

    private void verificarCampos(Paciente paciente) {
        if (paciente.getNombre() == null || paciente.getNombre().isBlank()) {
            throw new CampoInvalido("El campo 'Nombre' no puede ser vacío.");
        }
        if (paciente.getApellido() == null || paciente.getApellido().isBlank()) {
            throw new CampoInvalido("El campo 'Apellido' no puede ser vacío.");
        }
        if (paciente.getDni() == null || paciente.getDni().isBlank()) {
            throw new CampoInvalido("El campo 'DNI' no puede ser vacío.");
        }
        if (paciente.getTelefono() == null || paciente.getTelefono().isBlank()) {
            throw new CampoInvalido("El campo 'Número de Teléfono' no puede ser vacío.");
        }
    }
}
