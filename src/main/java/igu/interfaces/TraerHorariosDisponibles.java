package igu.interfaces;

import java.util.Date;
import java.util.Map;
import logica.clases.TipoConsulta;
import logica.clases.Turno;

public interface TraerHorariosDisponibles {

    public Map<Date, Date> traer(Date fecha, TipoConsulta tipoConsulta, Turno turnoIgnorar);

}
