package igu.clases_utilitarias;

import java.util.Objects;
import logica.clases.TipoConsulta;

//Esta clase sirve para guardar los tipos de consulta en el ComboBox
public class TipoDeConsulta {

    private TipoConsulta tipoConsulta;

    public TipoDeConsulta(TipoConsulta tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public TipoConsulta getTipoConsulta() {
        return tipoConsulta;
    }

    @Override
    public String toString() {
        return tipoConsulta.getNombreConsulta() + " - " + tipoConsulta.getDuracionMinutos() + "min - " + tipoConsulta.getCosto() + "ARS";
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoConsulta);
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
        final TipoDeConsulta other = (TipoDeConsulta) obj;
        return Objects.equals(this.tipoConsulta, other.tipoConsulta);
    }
}
