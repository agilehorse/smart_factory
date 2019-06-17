package exceptions;

public class NotEnoughWorkersException extends RuntimeException{

    public NotEnoughWorkersException(String message) {
        super(message);
    }
}
