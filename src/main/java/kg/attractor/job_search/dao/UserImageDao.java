package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.UserImage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserImageDao {
    private final JdbcTemplate jdbcTemplate;

    public Optional<UserImage> getImageByMovieId(Long userId) {
        String sql = "select * from user_image where user_id=?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{userId}, UserImage.class));
    }

}
