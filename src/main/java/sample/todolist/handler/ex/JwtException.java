package sample.todolist.handler.ex;

public class JwtException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public JwtException(String message) {
        super(message);
    }
}
