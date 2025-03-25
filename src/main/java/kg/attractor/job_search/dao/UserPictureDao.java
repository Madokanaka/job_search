package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.UserPicture;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPictureDao {
    private final JdbcTemplate jdbcTemplate;

    public Optional<UserPicture> getImageById(Long userId) {
        String sql = "select * from user_pictures where user_id=?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{userId}, UserPicture.class));
    }

    public void save(Long userId, String fileName) {
        String sql = "insert into user_pictures(user_id, file_name) values(?,?)";
        jdbcTemplate.update(sql, userId, fileName);

    }

}
