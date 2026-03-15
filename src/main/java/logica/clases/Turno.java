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

@Entity
public class Turno implements Serializable {

    public enum Estado {
        PENDIENTE {
            @Override
            public boolean puedeConfirmar() {
                return true;
            }

            @Override
            public boolean puedeAtender() {
                return false;
            }

            @Override
            public boolean puedeReprogramar() {
                return true;
            }

            @Override
            public boolean puedeCancelar() {
                return true;
            }

            @Override
            public boolean puedeAusentar() {
                return false;
            }
        }, CONFIRMADO {
            @Override
            public boolean puedeConfirmar() {
                return false;
            }

            @Override
            public boolean puedeAtender() {
                return true;
            }

            @Override
            public boolean puedeReprogramar() {
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
        }, CANCELADO {
            @Override
            public boolean puedeConfirmar() {
                return false;
            }

            @Override
            public boolean puedeAtender() {
                return false;
            }

            @Override
            public boolean puedeReprogramar() {
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
        }, ATENDIDO {
            @Override
            public boolean puedeConfirmar() {
                return false;
            }

            @Override
            public boolean puedeAtender() {
                return false;
            }

            @Override
            public boolean puedeReprogramar() {
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
        }, AUSENTADO {
            @Override
            public boolean puedeConfirmar() {
                return false;
            }

            @Override
            public boolean puedeAtender() {
                return false;
            }

            @Override
            public boolean puedeReprogramar() {
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
        };

        public abstract boolean puedeConfirmar();

        public abstract boolean puedeAtender();

        public abstract boolean puedeReprogramar();

        public abstract boolean puedeCancelar();

        public abstract boolean puedeAusentar();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long idTurno; //long para IDs es lo ideal

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date horaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date horaFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.PENDIENTE;

    @Column(nullable = false)
    private boolean reprogramado = false;

    @ManyToOne
    @JoinColumn(name = "idPaciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "idTipoConsulta")
    private TipoConsulta tipoConsulta;

    //JPA necesita el constructor vacío
    public Turno() {

    }

    public Turno(Date horaInicio, Date horaFinal, Paciente paciente, TipoConsulta tipoConsulta) {
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
        this.paciente = paciente;
        this.tipoConsulta = tipoConsulta;
    }

    public long getIdTurno() {
        return idTurno;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean isReprogramado() {
        return reprogramado;
    }

    public void setReprogramado(boolean reprogramado) {
        this.reprogramado = reprogramado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public TipoConsulta getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(TipoConsulta tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public boolean puedeConfirmar() {
        return estado.puedeConfirmar();
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
        return "Turno{" + "idTurno=" + idTurno + ", horaInicio=" + horaInicio + ", horaFinal=" + horaFinal + ", estado=" + estado + ", reprogramado=" + reprogramado + ", paciente=" + paciente + ", tipoConsulta=" + tipoConsulta + '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Turno otroTurno = (Turno) obj;

        return idTurno == otroTurno.idTurno;
    }
}
