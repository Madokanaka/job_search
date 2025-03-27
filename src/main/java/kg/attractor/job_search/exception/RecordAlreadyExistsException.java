package kg.attractor.job_search.exception;

public class RecordAlreadyExistsException extends RuntimeException {
    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
