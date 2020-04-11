package lt.okaminu.archiwix.parser;

public final class InvalidQueryException extends RuntimeException {
    public InvalidQueryException() {
        super();
    }

    public InvalidQueryException(String message) {
        super(message);
    }
}
