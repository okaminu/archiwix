package lt.okaminu.archiwix;

public final class InvalidQueryException extends RuntimeException {
    public InvalidQueryException() {
        super();
    }

    public InvalidQueryException(String message) {
        super(message);
    }
}
