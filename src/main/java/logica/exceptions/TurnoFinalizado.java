package logica.exceptions;

public class TurnoFinalizado extends RuntimeException {

    private String mensaje;

    public TurnoFinalizado() {
    }

    public TurnoFinalizado(String msg) {
        super(msg);
        mensaje = msg;
    }

    public String getMensaje() {
        return mensaje;
    }
}
