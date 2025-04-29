package kg.attractor.job_search.exception;

import java.util.NoSuchElementException;

public class CategoryNotFoundException extends NoSuchElementException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
