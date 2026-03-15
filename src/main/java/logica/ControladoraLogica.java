package logica;

import java.util.List;
import logica.clases.Paciente;
import logica.clases.TipoConsulta;
import logica.clases.Turno;
import persistencia.ControladoraPersistencia;
import persistencia.exceptions.NonexistentEntityException;

public class ControladoraLogica {

    private ControladoraPersistencia controladoraPersistencia = new ControladoraPersistencia();

    //Paciente////////////////////////////////////////////////////////////////////////////////////////////////////
    public void crearPaciente(Paciente paciente) {
        controladoraPersistencia.crearPaciente(paciente);
    }

    public List<Paciente> traerPacientes() {
        return controladoraPersistencia.traerPacientes();
    }

    public Paciente traerPaciente(long idPaciente) {
        return controladoraPersistencia.traerPaciente(idPaciente);
    }

    public void editarPaciente(Paciente paciente) throws Exception {
        controladoraPersistencia.editarPaciente(paciente);
    }

    public void eliminarPaciente(long idPaciente) throws NonexistentEntityException {
        controladoraPersistencia.eliminarPaciente(idPaciente);
    }

    //TipoConsulta////////////////////////////////////////////////////////////////////////////////////////////////////
    public void crearTipoConsulta(TipoConsulta tipoConsulta) {
        controladoraPersistencia.crearTipoConsulta(tipoConsulta);
    }

    public List<TipoConsulta> traerTiposConsulta() {
        return controladoraPersistencia.traerTiposConsulta();
    }

    public TipoConsulta traerTipoConsulta(long idTipoConsulta) {
        return controladoraPersistencia.traerTipoConsulta(idTipoConsulta);
    }

    public void editarTipoConsulta(TipoConsulta tipoConsulta) throws Exception {
        controladoraPersistencia.editarTipoConsulta(tipoConsulta);
    }

    public void eliminarTipoConsulta(long idTipoConsulta) throws NonexistentEntityException {
        controladoraPersistencia.eliminarTipoConsulta(idTipoConsulta);
    }

    //Turno////////////////////////////////////////////////////////////////////////////////////////////////////
    public void crearTurno(Turno turno) {
        controladoraPersistencia.crearTurno(turno);
    }

    public List<Turno> traerTurnos() {
        return controladoraPersistencia.traerTurnos();
    }

    public Turno traerTurno(long idTurno) {
        return controladoraPersistencia.traerTurno(idTurno);
    }

    public void editarTurno(Turno turno) throws Exception {
        controladoraPersistencia.editarTurno(turno);
    }
}
