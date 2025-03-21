package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setAge(rs.getInt("age"));
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setAvatar(rs.getString("avatar"));
            user.setAccountType(rs.getString("account_type"));
            user.setPassword(rs.getString("password")); // Добавляем пароля
            return user;
        });
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setAge(rs.getInt("age"));
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setAvatar(rs.getString("avatar"));
            user.setAccountType(rs.getString("account_type"));
            user.setPassword(rs.getString("password")); // Добавляем пароля
            return user;
        });
    }

    public int createUser(User user) {
        String sql = "INSERT INTO users (name, surname, age, email, phone_number, avatar, account_type, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getName(), user.getSurname(), user.getAge(), user.getEmail(),
                user.getPhoneNumber(), user.getAvatar(), user.getAccountType(), user.getPassword());
    }
}
