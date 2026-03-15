package logica.clases;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

//Esta clase solo es para guardar los horarios en el ComboBox
public class HorarioDisponible {

    private final Date horarioInicial;
    private final Date horarioFinal;

    public HorarioDisponible(Date horarioInicial, Date horarioFinal) {
        this.horarioInicial = horarioInicial;
        this.horarioFinal = horarioFinal;
    }

    public Date getHorarioInicial() {
        return horarioInicial;
    }

    public Date getHorarioFinal() {
        return horarioFinal;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
        return formatoHora.format(horarioInicial) + " - " + formatoHora.format(horarioFinal) + " hs";
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        HorarioDisponible otroHoraDisponible = (HorarioDisponible) obj;

        return horarioInicial.equals(otroHoraDisponible.horarioInicial) && horarioFinal.equals(otroHoraDisponible.horarioFinal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(horarioInicial, horarioFinal);
    }        
}
