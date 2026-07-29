package logica.exceptions;

public class TipoConsultaDeshabilitado extends RuntimeException {

    private String mensaje;

    public TipoConsultaDeshabilitado() {
    }

    public TipoConsultaDeshabilitado(String msg) {
        super(msg);
        mensaje = msg;
    }

    public String getMensaje() {
        return mensaje;
    }
}
