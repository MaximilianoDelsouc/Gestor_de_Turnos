package igu.interfaces;

import java.util.Date;
import logica.clases.Paciente;
import logica.clases.TipoConsulta;
import logica.clases.Turno;

public interface AccionesTurno {

    public void guardarNuevoTurno(Date fechaHoraInicial, Date fechaHoraFinal, Paciente paciente, TipoConsulta tipoConsulta);

    public void reprogramarTurno(Turno turnoReprogramar, Date fechaHoraInicial, Date fechaHoraFinal);

    public void cancelar();
}
