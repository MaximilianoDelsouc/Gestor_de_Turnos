package persistencia.exceptions;

public class ProblemaPersistencia extends RuntimeException {

    public ProblemaPersistencia() {

    }

    public ProblemaPersistencia(String message) {
        super(message);
    }
}
