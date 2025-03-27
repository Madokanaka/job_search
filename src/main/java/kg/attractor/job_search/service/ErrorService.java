package kg.attractor.job_search.service;

import kg.attractor.job_search.handler.ErrorResponseBody;
import org.springframework.validation.BindingResult;

public interface ErrorService {
    ErrorResponseBody makeResponse(Exception ex);

    ErrorResponseBody makeResponse(BindingResult bindingResult);
}
