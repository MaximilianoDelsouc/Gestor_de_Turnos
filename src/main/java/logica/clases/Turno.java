package logica.clases;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import logica.exceptions.CampoInvalido;

@Entity
@Table(name = "turnos")
public class Turno implements Serializable {

    public enum Estado {
        PENDIENTE {
            @Override
            public boolean puedeAtender() {
                return true;
            }

            @Override
            public boolean puedeCancelar() {
                return true;
            }

            @Override
            public boolean puedeAusentar() {
                return true;
            }

            @Override
            public boolean puedeReprogramar() {
                return false;
            }
        }, ATENDIDO {
            @Override
            public boolean puedeAtender() {
                return false;
            }

            @Override
            public boolean puedeCancelar() {
                return false;
            }

            @Override
            public boolean puedeAusentar() {
                return false;
            }

            @Override
            public boolean puedeReprogramar() {
                return false;
            }
        }, CANCELADO {
            @Override
            public boolean puedeAtender() {
                return false;
            }

            @Override
            public boolean puedeCancelar() {
                return false;
            }

            @Override
            public boolean puedeAusentar() {
                return false;
            }

            @Override
            public boolean puedeReprogramar() {
                return true;
            }
        }, AUSENTADO {
            @Override
            public boolean puedeAtender() {
                return false;
            }

            @Override
            public boolean puedeCancelar() {
                return false;
            }

            @Override
            public boolean puedeAusentar() {
                return false;
            }

            @Override
            public boolean puedeReprogramar() {
                return false;
            }
        };

        public abstract boolean puedeAtender();

        public abstract boolean puedeCancelar();

        public abstract boolean puedeAusentar();

        public abstract boolean puedeReprogramar();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private long idTurno;

    @Temporal(TemporalType.TIMESTAMP) // TIMESTAMP persiste fecha y hora
    @Column(name = "fecha_hora_inicial", nullable = false)
    private Date fechaHoraInicial;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_hora_final", nullable = false)
    private Date fechaHoraFinal;

    @Enumerated(EnumType.STRING) // Con STRING se persiste el nombre del enum. Es lo recomendado casi siempre
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.PENDIENTE;

    @Column(name = "reprogramado", nullable = false)
    private boolean reprogramado = false;

    @ManyToOne(optional = true) // optional = true le indica a JPA que la asociación no es obligatoria. O sea, puede existir null
    @JoinColumn(name = "id_paciente", nullable = true)
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tipo_consulta", nullable = false)
    private TipoConsulta tipoConsulta;

    public Turno() {

    }

    public Turno(Date fechaHoraInicial, Date fechaHoraFinal, Paciente paciente, TipoConsulta tipoConsulta) {
        verificarFechasHoras(fechaHoraInicial, fechaHoraFinal);
        this.fechaHoraInicial = fechaHoraInicial;
        this.fechaHoraFinal = fechaHoraFinal;

        this.paciente = verificarPaciente(paciente);
        this.tipoConsulta = verificarTipoConsulta(tipoConsulta);
    }

    public long getIdTurno() {
        return idTurno;
    }

    // Los setters para las fechas y horas no han sido agregados porque al reprogramar un turno se crea una nueva instancia
    public Date getFechaHoraInicial() {
        return fechaHoraInicial;
    }

    public Date getFechaHoraFinal() {
        return fechaHoraFinal;
    }

    private static void verificarFechasHoras(Date fechaHoraInicial, Date fechaHoraFinal) {
        if (fechaHoraInicial == null || fechaHoraFinal == null) {
            throw new CampoInvalido("Las fechas y horas inicial y final del turno no deben ser vacías.");
        }

        /*
        Date fechaHoraActual = new Date();
        if (fechaHoraInicial.before(fechaHoraActual)) {
            throw new CampoInvalido("Las fecha y hora inicial del turno no deben ser anterior a la fecha y hora actual.");
        }
         */
        if (!fechaHoraFinal.after(fechaHoraInicial)) {
            throw new CampoInvalido("La fecha y hora final del turno no debe ser anterior a su fecha y hora inicial.");
        }
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = verificarEstado(estado);
    }

    private Estado verificarEstado(Estado estado) {
        if (estado == null) {
            throw new CampoInvalido("El estado del turno no debe ser vacío.");
        }

        if (estado == Estado.PENDIENTE) {
            throw new CampoInvalido("El estado del turno no debe cambiarse a Pendiente ya que este es el valor inicial y por defecto.");
        }

        if (estado == Estado.ATENDIDO && !this.estado.puedeAtender()) {
            throw new CampoInvalido("El turno solo se puede atender si se encuentra pendiente.");
        }

        if (estado == Estado.CANCELADO && !this.estado.puedeCancelar()) {
            throw new CampoInvalido("El turno solo se puede cancelar si se encuentra pendiente.");
        }

        if (estado == Estado.AUSENTADO && !this.estado.puedeAusentar()) {
            throw new CampoInvalido("El turno solo se puede ausentar si se encuentra pendiente.");
        }

        return estado;
    }

    public boolean isReprogramado() {
        return reprogramado;
    }

    public void setReprogramado(boolean reprogramado) {
        this.reprogramado = verificarReprogramado(reprogramado);
    }

    private boolean verificarReprogramado(boolean reprogramado) {
        if (this.reprogramado && !reprogramado) { // Solo si el turno ya está reprogramado. Para evitar problemas de JPA hydrate
            throw new CampoInvalido("Si el turno ya fue reprogramado no se puede deshacer.");
        }

        if (reprogramado && !this.estado.puedeReprogramar()) {
            throw new CampoInvalido("El turno solo se puede reprogramar si ha sido previamente cancelado.");
        }

        return reprogramado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    // Este método solo existe para el método destroy del JPAController
    public void setPaciente(Paciente paciente) {
        if (paciente != null) {
            throw new IllegalArgumentException("El paciente de un turno solo puede establecerse como null.");
        }

        this.paciente = null;
    }

    private static Paciente verificarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new CampoInvalido("El paciente del turno no debe ser vacío");
        }

        return paciente;
    }

    public TipoConsulta getTipoConsulta() {
        return tipoConsulta;
    }

    private static TipoConsulta verificarTipoConsulta(TipoConsulta tipoConsulta) {
        if (tipoConsulta == null) {
            throw new CampoInvalido("El tipo de consulta del turno no debe ser vacío.");
        }

        if (!tipoConsulta.isHabilitado()) {
            throw new CampoInvalido("El tipo de consulta del turno no debe estar deshabilitado.");
        }

        return tipoConsulta;
    }

    public boolean puedeAtender() {
        return estado.puedeAtender();
    }

    public boolean puedeReprogramar() {
        return estado.puedeReprogramar();
    }

    public boolean puedeCancelar() {
        return estado.puedeCancelar();
    }

    public boolean puedeAusentar() {
        return estado.puedeAusentar();
    }

    @Override
    public String toString() {
        return "Turno{" + "idTurno=" + idTurno + ", fechaHoraInicio=" + fechaHoraInicial + ", fechaHoraFinal=" + fechaHoraFinal + ", estado=" + estado + ", reprogramado=" + reprogramado + ", paciente=" + paciente.getIdPaciente() + ", tipoConsulta=" + tipoConsulta.getIdTipoConsulta() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (int) (this.idTurno ^ (this.idTurno >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Turno other = (Turno) obj;
        return this.idTurno == other.idTurno;
    }
}
