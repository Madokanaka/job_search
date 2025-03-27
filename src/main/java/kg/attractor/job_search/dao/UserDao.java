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
        List<User> users = jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setAge(rs.getInt("age"));
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setAvatar(rs.getString("avatar"));
            user.setAccountType(rs.getString("account_type"));
            user.setPassword(rs.getString("password"));
            return user;
        });

        return users.isEmpty() ? null : users.get(0);
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
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    public int createUser(User user) {
        String sql = "INSERT INTO users (name, surname, age, email, phone_number, avatar, account_type, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getName(), user.getSurname(), user.getAge(), user.getEmail(),
                user.getPhoneNumber(), user.getAvatar(), user.getAccountType(), user.getPassword());
    }

    public User findById(Integer userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setAge(rs.getInt("age"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setAvatar(rs.getString("avatar"));
                user.setAccountType(rs.getString("account_type"));
                user.setPassword(rs.getString("password"));
                return user;
            });
        } catch (Exception e) {
            return null;
        }
    }

    public int updateUser(User user) {
        String sql = "UPDATE users SET name = ?, surname = ?, email = ?, phone_number = ?, avatar = ?, account_type = ?, password = ?, age = ? WHERE id = ?";
        return jdbcTemplate.update(sql, user.getName(), user.getSurname(), user.getEmail(),
                user.getPhoneNumber(), user.getAvatar(), user.getAccountType(), user.getPassword(), user.getAge(), user.getId());
    }

    public List<User> getApplicantsForVacancy(Integer vacancyId) {
        String sql = """
                    SELECT u.id, u.name, u.surname, u.age, u.email, u.phone_number, u.avatar, u.account_type, u.password
                    FROM users u
                    JOIN resumes r ON u.id = r.applicant_id
                    JOIN responded_applicants ra ON r.id = ra.resume_id
                    WHERE ra.vacancy_id = ?
                """;

        return jdbcTemplate.query(sql, new Object[]{vacancyId}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setAge(rs.getInt("age"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setAvatar(rs.getString("avatar"));
            user.setAccountType(rs.getString("account_type"));
            return user;
        });
    }

}
