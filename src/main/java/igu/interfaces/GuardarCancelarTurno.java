package igu.interfaces;

import logica.clases.Turno;

public interface GuardarCancelarTurno {

    public void eventoGuardarTurnoNuevo(Turno turno);

    public void eventoReprogramarTurno(Turno turnoReprogramado, Turno nuevoTurno);

    public void eventoCancelar();
}
