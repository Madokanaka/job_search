package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
    SELECT u FROM User u
    JOIN Resume r ON r.applicant = u
    JOIN RespondedApplicant ra ON ra.resume = r
    WHERE ra.vacancy.id = :vacancyId
""")
    List<User> findApplicantsByVacancyId(@Param("vacancyId") Integer vacancyId);

    Integer getIdByEmail(String email);

    Page<User> findByAccountType(String accountType, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.avatar = :fileName WHERE u.id = :userId")
    void saveAvatar(@Param("userId") Long userId, @Param("fileName") String fileName);

    Optional<User> findByResetPasswordToken(String token);}
