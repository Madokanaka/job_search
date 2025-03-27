package kg.attractor.job_search.exception;

import java.util.NoSuchElementException;

public class ResumeNotFoundException extends NoSuchElementException {
    public ResumeNotFoundException(String message) {
        super(message);
    }
}
