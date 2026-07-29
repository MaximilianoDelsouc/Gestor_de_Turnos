package logica.clases;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import logica.exceptions.CampoInvalido;
import logica.exceptions.TipoConsultaDeshabilitado;

@Entity
@Table(name = "tipos_consulta")
public class TipoConsulta implements Serializable {

    public static final int LONGITUD_MAXIMA_NOMBRE_CONSULTA = 90;
    public static final int DURACION_MAXIMA_MINUTOS = 480; // 480

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_consulta")
    private long idTipoConsulta;

    @Column(name = "nombre_consulta", nullable = false, length = LONGITUD_MAXIMA_NOMBRE_CONSULTA, unique = true)
    private String nombreConsulta;

    @Column(name = "duracion_minutos", nullable = false)
    private int duracionMinutos;

    @Column(name = "costo", nullable = false)
    private int costo;

    // En caso de que se desee 'eliminar' el tipo de consulta se conservará si tiene registros hijos
    @Column(name = "habilitado", nullable = false)
    private boolean habilitado = true;

    public TipoConsulta() {

    }

    public TipoConsulta(String nombreConsulta, int duracionMinutos, int costo) {
        this.nombreConsulta = verificarNombreConsulta(nombreConsulta);
        this.duracionMinutos = verificarDuracionMinutos(duracionMinutos);
        this.costo = verificarCosto(costo);
    }

    public long getIdTipoConsulta() {
        return idTipoConsulta;
    }

    public String getNombreConsulta() {
        return nombreConsulta;
    }

    public void setNombreConsulta(String nombreConsulta) {
        verificarHabilitado();
        this.nombreConsulta = verificarNombreConsulta(nombreConsulta);

    }

    private static String verificarNombreConsulta(String nombreConsulta) {
        if (nombreConsulta == null || nombreConsulta.isBlank()) {
            throw new CampoInvalido("El nombre de la consulta no debe ser vacío.");
        }

        nombreConsulta = nombreConsulta.strip();
        if (nombreConsulta.length() > LONGITUD_MAXIMA_NOMBRE_CONSULTA) {
            throw new CampoInvalido("El nombre de la consulta no debe superar " + LONGITUD_MAXIMA_NOMBRE_CONSULTA + " caracteres de longitud.");
        }

        return nombreConsulta;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        verificarHabilitado();
        this.duracionMinutos = verificarDuracionMinutos(duracionMinutos);
    }

    private static int verificarDuracionMinutos(int duracionMinutos) {
        if (duracionMinutos <= 0) {
            throw new CampoInvalido("La duración en minutos del tipo de consulta debe ser superior a 0.");
        }

        if (duracionMinutos % 10 != 0) {
            throw new CampoInvalido("La duración en minutos del tipo de consulta debe ser un valor de 10 en 10 (múltiplo de 10).");
        }

        if (duracionMinutos > DURACION_MAXIMA_MINUTOS) {
            float horas = (float) DURACION_MAXIMA_MINUTOS / 60;
            throw new CampoInvalido("La duración en minutos del tipo de consulta no debe superar los " + DURACION_MAXIMA_MINUTOS
                    + " minutos (" + horas + " horas).");
        }

        return duracionMinutos;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        verificarHabilitado();
        this.costo = verificarCosto(costo);
    }

    private static int verificarCosto(int costo) {
        if (costo < 0) {
            throw new CampoInvalido("El costo del tipo de consulta no debe ser un valor negativo.");
        }

        return costo;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    private void verificarHabilitado() {
        if (!habilitado) {
            throw new TipoConsultaDeshabilitado("No se puede modificar un tipo de consulta deshabilitado.");
        }
    }

    @Override
    public String toString() {
        return "TipoConsulta{" + "idTipoConsulta=" + idTipoConsulta + ", nombreConsulta=" + nombreConsulta + ", duracionMinutos=" + duracionMinutos + ", costo=" + costo + ", activo=" + habilitado + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.idTipoConsulta ^ (this.idTipoConsulta >>> 32));
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
        final TipoConsulta other = (TipoConsulta) obj;
        return this.idTipoConsulta == other.idTipoConsulta;
    }
}
