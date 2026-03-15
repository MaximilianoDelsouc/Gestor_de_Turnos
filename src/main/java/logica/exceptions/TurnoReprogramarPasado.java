package logica.exceptions;

public class TurnoReprogramarPasado extends RuntimeException {

    private String mensaje;

    public TurnoReprogramarPasado() {
    }

    public TurnoReprogramarPasado(String msg) {
        super(msg);
        mensaje = msg;
    }

    public String getMensaje() {
        return mensaje;
    }
}
