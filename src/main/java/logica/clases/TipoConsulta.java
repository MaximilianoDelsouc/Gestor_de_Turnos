package logica.clases;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TipoConsulta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long idTipoConsulta; //long para IDs es lo ideal

    @Column(nullable = false)
    private String nombreConsulta;

    @Column(nullable = false)
    private int duracionMinutos;

    @Column(nullable = false)
    private int costo;

    //JPA necesita el constructor vacío
    public TipoConsulta() {

    }

    public TipoConsulta(String nombreConsulta, int duracionMinutos, int costo) {
        this.nombreConsulta = nombreConsulta;
        this.duracionMinutos = duracionMinutos;
        this.costo = costo;
    }

    public long getIdTipoConsulta() {
        return idTipoConsulta;
    }

    public String getNombreConsulta() {
        return nombreConsulta;
    }

    public void setNombreConsulta(String nombreConsulta) {
        this.nombreConsulta = nombreConsulta;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return nombreConsulta + " - " + String.valueOf(duracionMinutos) + "min";
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TipoConsulta otroTipoConsulta = (TipoConsulta) obj;

        return idTipoConsulta == otroTipoConsulta.idTipoConsulta;
    }
}
