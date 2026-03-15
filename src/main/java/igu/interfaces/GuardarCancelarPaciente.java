package igu.interfaces;

import logica.clases.Paciente;

public interface GuardarCancelarPaciente {

    public void eventoGuardarPacienteNuevo(Paciente paciente);

    public void eventoGuardarPacienteEditado(Paciente paciente);

    public void eventoCancelar();
}
