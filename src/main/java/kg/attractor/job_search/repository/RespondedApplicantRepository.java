package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.RespondedApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RespondedApplicantRepository extends JpaRepository<RespondedApplicant, Integer> {
    Optional<RespondedApplicant> findByResumeIdAndVacancyId(Integer resumeId, Integer vacancyId);

}
