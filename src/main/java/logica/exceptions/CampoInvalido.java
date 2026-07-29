package logica.exceptions;

public class CampoInvalido extends RuntimeException {
        
    private String mensaje;
    
    public CampoInvalido() {
    }

    public CampoInvalido(String msg) {
        super(msg);
        mensaje = msg;
    }        

    public String getMensaje() {
        return mensaje;
    }        
}
