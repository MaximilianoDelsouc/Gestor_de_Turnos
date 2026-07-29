package logica;

import java.util.List;
import logica.clases.Paciente;
import logica.clases.Turno;
import logica.exceptions.CampoInvalido;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.ProblemaPersistencia;

public class LogicaPaciente {

    private final ControladoraLogica controladoraLogica; // Es final porque solo se asigna en el constructor

    public LogicaPaciente(ControladoraLogica controladoraLogica) {
        this.controladoraLogica = controladoraLogica;
    }

    /*
    CREATE
     */
    public void crearNuevo(String nombre, String apellido, String dni, String telefono, String correoElectronico, String observacion) {
        if (!comprobarUnicidadDNI(dni)) {
            throw new CampoInvalido("El número de DNI para el nuevo paciente ya existe en el sistema. Este debe ser único.");
        }

        Paciente nuevoPaciente = new Paciente(nombre, apellido, dni, telefono, correoElectronico, observacion);
        controladoraLogica.crearPaciente(nuevoPaciente);
    }

    /*
    READ
     */
    public List<Paciente> traerTodos() {
        return controladoraLogica.traerPacientes();
    }

    public Paciente traerPaciente(long idPaciente) {
        return controladoraLogica.traerPaciente(idPaciente);
    }

    public List<Paciente> buscar(String nombre, String apellido, String dni) {
        // Esto es para evitar hacer .toLowerCase() en cada iteración
        String nombreBuscado = (nombre == null || nombre.isBlank()) ? null : nombre.toLowerCase(); // effectively final
        String apellidoBuscado = (apellido == null || apellido.isBlank()) ? null : apellido.toLowerCase();
        String dniBuscado = (dni == null || dni.isBlank()) ? null : dni;

        return traerTodos().stream()
                .filter(paciente -> nombreBuscado == null || paciente.getNombre().toLowerCase().contains(nombreBuscado)) // Si el campo buscado es null, pasan todos porque la condición siempre será true
                .filter(paciente -> apellidoBuscado == null || paciente.getApellido().toLowerCase().contains(apellidoBuscado))
                .filter(paciente -> dniBuscado == null || paciente.getDni().contains(dniBuscado))
                .toList();
    }

    public List<Turno> traerHistorialCompletoPaciente(long idPaciente) {
        return controladoraLogica.traerPaciente(idPaciente).getTurnos();
    }

    public List<Turno> traerHistorialTurnosAtendidosPaciente(long idPaciente) {
        return controladoraLogica.traerPaciente(idPaciente).getTurnos().stream()
                .filter(turno -> turno.getEstado() == Turno.Estado.ATENDIDO)
                .toList();
    }

    public List<Turno> traerHistorialTurnosCanceladosAusentadosPaciente(long idPaciente) {
        return controladoraLogica.traerPaciente(idPaciente).getTurnos().stream()
                .filter(turno -> turno.getEstado() == Turno.Estado.CANCELADO
                || turno.getEstado() == Turno.Estado.AUSENTADO)
                .toList();
    }

    /*
    UPDATE
     */
    public void editarDatos(Paciente pacienteEditar, String nombre, String apellido, String dni, String telefono, String correoElectronico, String observacion) {
        if (!comprobarUnicidadDNI(dni)) {
            throw new CampoInvalido("El número de DNI para el paciente a editar ya existe en el sistema. Este debe ser único.");
        }

        pacienteEditar.setNombre(nombre);
        pacienteEditar.setApellido(apellido);
        pacienteEditar.setDni(dni);
        pacienteEditar.setTelefono(telefono);
        pacienteEditar.setCorreoElectronico(correoElectronico);
        pacienteEditar.setObservacion(observacion);

        try {
            controladoraLogica.editarPaciente(pacienteEditar);
        } catch (Exception e) {
            throw new ProblemaPersistencia();
        }
    }

    private boolean comprobarUnicidadDNI(String dni) {
        return !controladoraLogica.traerPacientes().stream()
                .anyMatch(paciente -> paciente.getDni().equals(dni));
    }

    /*
    DELETE
     */
    public void eliminar(long idPaciente) {
        try {
            controladoraLogica.eliminarPaciente(idPaciente);

        } catch (NonexistentEntityException e) {
            throw new ProblemaPersistencia("El paciente que se intenta eliminar no existe.");
        }
    }
}
