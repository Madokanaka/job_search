package kg.attractor.job_search.service;

import kg.attractor.job_search.model.ContactType;
import java.util.Optional;

public interface ContactTypeService {
    boolean existsById(Integer id);

    Optional<ContactType> findByType(String type);

    Optional<ContactType> findById(Integer id);
}