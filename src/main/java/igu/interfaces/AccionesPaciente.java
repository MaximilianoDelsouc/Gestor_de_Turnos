package igu.interfaces;

import logica.clases.Paciente;

public interface AccionesPaciente {

    public void guardarNuevoPaciente(String nombre, String apellido, String dni, String telefono, String correoElectronico, String observacion);

    public void guardarPacienteEditado(Paciente pacienteEditar, String nombre, String apellido, String dni, String telefono, String correoElectronico, String observacion);

    public void eventoCancelar();
}
