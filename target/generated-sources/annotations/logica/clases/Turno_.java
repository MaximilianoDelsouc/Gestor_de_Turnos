package logica.clases;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.clases.Paciente;
import logica.clases.TipoConsulta;
import logica.clases.Turno.Estado;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-07-29T10:45:47", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Turno.class)
public class Turno_ { 

    public static volatile SingularAttribute<Turno, Estado> estado;
    public static volatile SingularAttribute<Turno, Date> fechaHoraInicial;
    public static volatile SingularAttribute<Turno, TipoConsulta> tipoConsulta;
    public static volatile SingularAttribute<Turno, Paciente> paciente;
    public static volatile SingularAttribute<Turno, Date> fechaHoraFinal;
    public static volatile SingularAttribute<Turno, Boolean> reprogramado;
    public static volatile SingularAttribute<Turno, Long> idTurno;

}