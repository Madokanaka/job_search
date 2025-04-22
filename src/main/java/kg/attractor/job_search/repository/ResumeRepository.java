package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    boolean existsByApplicantId(Integer applicantId);
    boolean existsByCategoryId(Integer categoryId);
    List<Resume> findByApplicantId(Integer applicantId);

    Page<Resume> findAll(Pageable pageable);
    Page<Resume> findByApplicantId(Integer applicantId, Pageable pageable);
}
