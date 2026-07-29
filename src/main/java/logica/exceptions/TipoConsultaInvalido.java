package logica.exceptions;

public class TipoConsultaInvalido extends RuntimeException {

    private String mensaje;

    public TipoConsultaInvalido() {
    }

    public TipoConsultaInvalido(String msg) {
        super(msg);
        mensaje = msg;
    }

    public String getMensaje() {
        return mensaje;
    }
}
