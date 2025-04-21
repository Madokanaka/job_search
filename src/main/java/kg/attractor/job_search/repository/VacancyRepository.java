package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Integer> {

    List<Vacancy> findByAuthorId(Integer authorId);

    List<Vacancy> findByCategoryId(Integer categoryId);

    @Query("""
    SELECT v FROM Vacancy v
    JOIN RespondedApplicant ra ON ra.vacancy.id = v.id
    JOIN Resume r ON ra.resume.id = r.id
    WHERE r.applicant.id = :userId AND v.isActive = true
""")
    List<Vacancy> findVacanciesUserRespondedTo(@Param("userId") Integer userId);

    Page<Vacancy> findAll(Pageable pageable);

    Page<Vacancy> findByAuthorId(Integer authorId, Pageable pageable);

    Page<Vacancy> findByCategoryId(Integer categoryId, Pageable pageable);

}
