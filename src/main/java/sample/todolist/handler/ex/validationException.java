package sample.todolist.handler.ex;

public class validationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public validationException(String message) {
        super(message);
    }

}
