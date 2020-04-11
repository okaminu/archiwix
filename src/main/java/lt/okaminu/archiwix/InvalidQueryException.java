package lt.okaminu.archiwix;

public final class InvalidQueryException extends RuntimeException {
    protected InvalidQueryException() {
        super();
    }

    protected InvalidQueryException(String message) {
        super(message);
    }
}
