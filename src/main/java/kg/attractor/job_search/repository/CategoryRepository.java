package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c.name FROM Category c WHERE c.id = :id")
    Optional<String> findNameById(@Param("id") Integer id);
}
