package persistencia;

import java.util.List;
import logica.clases.Paciente;
import logica.clases.TipoConsulta;
import logica.clases.Turno;
import persistencia.exceptions.NonexistentEntityException;

public class ControladoraPersistencia {

    private PacienteJpaController pacienteJpaController = new PacienteJpaController();
    private TipoConsultaJpaController tipoConsultaJpaController = new TipoConsultaJpaController();
    private TurnoJpaController turnoJpaController = new TurnoJpaController();

    //Paciente////////////////////////////////////////////////////////////////////////////////////////////////////
    public void crearPaciente(Paciente paciente) {
        pacienteJpaController.create(paciente);
    }

    public List<Paciente> traerPacientes() {
        return pacienteJpaController.findPacienteEntities();
    }

    public Paciente traerPaciente(long idPaciente) {
        return pacienteJpaController.findPaciente(idPaciente);
    }

    public void editarPaciente(Paciente paciente) throws Exception {
        pacienteJpaController.edit(paciente);
    }

    public void eliminarPaciente(long idPaciente) throws NonexistentEntityException {
        pacienteJpaController.destroy(idPaciente);
    }

    //TipoConsulta////////////////////////////////////////////////////////////////////////////////////////////////////
    public void crearTipoConsulta(TipoConsulta tipoConsulta) {
        tipoConsultaJpaController.create(tipoConsulta);
    }

    public List<TipoConsulta> traerTiposConsulta() {
        return tipoConsultaJpaController.findTipoConsultaEntities();
    }

    public TipoConsulta traerTipoConsulta(long idTipoConsulta) {
        return tipoConsultaJpaController.findTipoConsulta(idTipoConsulta);
    }

    public void editarTipoConsulta(TipoConsulta tipoConsulta) throws Exception {
        tipoConsultaJpaController.edit(tipoConsulta);
    }

    public void eliminarTipoConsulta(long idTipoConsulta) throws NonexistentEntityException {
        tipoConsultaJpaController.destroy(idTipoConsulta);
    }

    //Turno////////////////////////////////////////////////////////////////////////////////////////////////////
    public void crearTurno(Turno turno) {
        turnoJpaController.create(turno);
    }

    public List<Turno> traerTurnos() {
        return turnoJpaController.findTurnoEntities();
    }

    public Turno traerTurno(long idTurno) {
        return turnoJpaController.findTurno(idTurno);
    }

    public void editarTurno(Turno turno) throws Exception {
        turnoJpaController.edit(turno);
    }
}
