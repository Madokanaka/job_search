package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactTypeRepository extends JpaRepository<ContactType, Integer> {
    Optional<ContactType> findByType(String Type);
}
