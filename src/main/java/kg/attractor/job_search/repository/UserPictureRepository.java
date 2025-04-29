package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.UserPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPictureRepository extends JpaRepository<UserPicture, Long> {

    Optional<UserPicture> findByUserId(Integer userId);
}
