package logica.clases;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import logica.clases.Turno;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-07-29T10:45:47", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Paciente.class)
public class Paciente_ { 

    public static volatile SingularAttribute<Paciente, Long> idPaciente;
    public static volatile SingularAttribute<Paciente, String> apellido;
    public static volatile ListAttribute<Paciente, Turno> turnos;
    public static volatile SingularAttribute<Paciente, String> telefono;
    public static volatile SingularAttribute<Paciente, String> nombre;
    public static volatile SingularAttribute<Paciente, String> correoElectronico;
    public static volatile SingularAttribute<Paciente, String> dni;
    public static volatile SingularAttribute<Paciente, String> observacion;

}