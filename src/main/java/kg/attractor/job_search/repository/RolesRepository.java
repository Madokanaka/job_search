package kg.attractor.job_search.repository;

import jakarta.validation.constraints.Pattern;
import kg.attractor.job_search.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {

    public boolean existsByRole(String role);

    public Role findByRole(String role);
}
