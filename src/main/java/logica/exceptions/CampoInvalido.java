package logica.exceptions;

public class CampoInvalido extends RuntimeException {
        
    private String mensaje;
    
    public CampoInvalido() {
    }

    public CampoInvalido(String message) {
        super(message);
        mensaje = message;
    }        

    public String getMensaje() {
        return mensaje;
    }        
}
