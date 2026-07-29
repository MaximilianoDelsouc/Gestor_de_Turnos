package logica.exceptions;

public class HorarioInvalido extends RuntimeException {

    private String mensaje;

    public HorarioInvalido() {
    }

    public HorarioInvalido(String msg) {
        super(msg);
        mensaje = msg;
    }

    public String getMensaje() {
        return mensaje;
    }
}
