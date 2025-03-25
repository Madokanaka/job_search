package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.Vacancy;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VacancyDao {

    private final JdbcTemplate jdbcTemplate;


    public int createVacancy(Vacancy vacancy) {
        String sql = "INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        return jdbcTemplate.update(sql,
                vacancy.getName(),
                vacancy.getDescription(),
                vacancy.getCategoryId(),
                vacancy.getSalary(),
                vacancy.getExpFrom(),
                vacancy.getExpTo(),
                vacancy.getIsActive(),
                vacancy.getAuthorId());
    }

    public boolean updateVacancy(Integer vacancyId, Vacancy vacancy) {
        String sql = "UPDATE vacancies SET name = ?, description = ?, salary = ?, exp_from = ?, exp_to = ?, is_active = ?, update_time = NOW() WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                vacancy.getName(),
                vacancy.getDescription(),
                vacancy.getSalary(),
                vacancy.getExpFrom(),
                vacancy.getExpTo(),
                vacancy.getIsActive(),
                vacancyId);
        return rowsAffected > 0;
    }

    public boolean deleteVacancy(Integer vacancyId) {
        String sql = "DELETE FROM vacancies WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, vacancyId);
        return rowsAffected > 0;
    }


    public List<Vacancy> getAllVacancies() {
        String sql = "SELECT * FROM vacancies WHERE is_active = true";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Vacancy(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getDouble("salary"),
                rs.getInt("exp_from"),
                rs.getInt("exp_to"),
                rs.getBoolean("is_active"),
                rs.getInt("author_id"),
                rs.getTimestamp("created_date").toLocalDateTime(),
                rs.getTimestamp("update_time").toLocalDateTime()
        ));
    }

    public Optional<Vacancy> getVacancyById(Integer vacancyId) {
        String sql = "SELECT * FROM vacancies WHERE id = ?";
        List<Vacancy> result = jdbcTemplate.query(sql, new Object[]{vacancyId}, (rs, rowNum) -> new Vacancy(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getDouble("salary"),
                rs.getInt("exp_from"),
                rs.getInt("exp_to"),
                rs.getBoolean("is_active"),
                rs.getInt("author_id"),
                rs.getTimestamp("created_date").toLocalDateTime(),
                rs.getTimestamp("update_time").toLocalDateTime()
        ));
        return result.stream().findFirst();
    }

    public List<Vacancy> getVacanciesByCategory(Integer categoryId) {
        String sql = "SELECT * FROM vacancies WHERE category_id = ? AND is_active = true";
        return jdbcTemplate.query(sql, new Object[]{categoryId}, (rs, rowNum) -> new Vacancy(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getDouble("salary"),
                rs.getInt("exp_from"),
                rs.getInt("exp_to"),
                rs.getBoolean("is_active"),
                rs.getInt("author_id"),
                rs.getTimestamp("created_date").toLocalDateTime(),
                rs.getTimestamp("update_time").toLocalDateTime()
        ));
    }

    public List<Vacancy> getVacanciesUserRespondedTo(Integer userId) {
        String sql = """
                    SELECT v.* FROM vacancies v
                    JOIN responded_applicants ra ON v.id = ra.vacancy_id
                    JOIN resumes r ON ra.resume_id = r.id
                    WHERE r.applicant_id = ? AND v.is_active = true
                """;
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> new Vacancy(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getDouble("salary"),
                rs.getInt("exp_from"),
                rs.getInt("exp_to"),
                rs.getBoolean("is_active"),
                rs.getInt("author_id"),
                rs.getTimestamp("created_date").toLocalDateTime(),
                rs.getTimestamp("update_time").toLocalDateTime()
        ));
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

    public boolean existsUserById(Integer userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    public boolean existsCategoryById(Integer categoryId) {
        String sql = "SELECT COUNT(*) FROM categories WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
        return count != null && count > 0;
    }

    public boolean isUserEmployer (Integer userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ? and";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }
}
