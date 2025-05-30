package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.RespondedApplicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RespondedApplicantRepository extends JpaRepository<RespondedApplicant, Integer> {
    Optional<RespondedApplicant> findByResumeIdAndVacancyId(Integer resumeId, Integer vacancyId);


    Page<RespondedApplicant> findByResumeApplicantId(Integer applicantId, Pageable pageable);

    Page<RespondedApplicant> findByVacancyAuthorId(Integer employerId, Pageable pageable);
}
