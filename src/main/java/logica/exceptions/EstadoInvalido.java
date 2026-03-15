package logica.exceptions;

public class EstadoInvalido extends RuntimeException {

    private String mensaje;

    public EstadoInvalido() {
    }

    public EstadoInvalido(String msg) {
        super(msg);
        mensaje = msg;
    }

    public String getMensaje() {
        return mensaje;
    }
}
