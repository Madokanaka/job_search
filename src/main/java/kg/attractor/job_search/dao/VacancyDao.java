package kg.attractor.job_search.dao;

import kg.attractor.job_search.dto.VacancyDto;

import kg.attractor.job_search.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VacancyDao {

    private final JdbcTemplate jdbcTemplate;


    public void createVacancy(VacancyDto vacancyDto) {
        String sql = "INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, is_active, author_id, created_date, update_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        jdbcTemplate.update(sql,
                vacancyDto.getName(),
                vacancyDto.getDescription(),
                vacancyDto.getCategoryId(),
                vacancyDto.getSalary(),
                vacancyDto.getExpFrom(),
                vacancyDto.getExpTo(),
                vacancyDto.getIsActive(),
                vacancyDto.getAuthorId());
    }

    public void updateVacancy(Integer vacancyId, VacancyDto vacancyDto) {
        String sql = "UPDATE vacancies SET name = ?, description = ?, category_id = ?, salary = ?, exp_from = ?, exp_to = ?, is_active = ?, update_time = NOW() WHERE id = ?";
        jdbcTemplate.update(sql,
                vacancyDto.getName(),
                vacancyDto.getDescription(),
                vacancyDto.getCategoryId(),
                vacancyDto.getSalary(),
                vacancyDto.getExpFrom(),
                vacancyDto.getExpTo(),
                vacancyDto.getIsActive(),
                vacancyId);
    }

    public void deleteVacancy(Integer vacancyId) {
        String sql = "DELETE FROM vacancies WHERE id = ?";
        jdbcTemplate.update(sql, vacancyId);
    }


    public List<VacancyDto> getAllVacancies() {
        String sql = "SELECT * FROM vacancies WHERE is_active = true";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new VacancyDto(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getDouble("salary"),
                rs.getInt("exp_from"),
                rs.getInt("exp_to"),
                rs.getBoolean("is_active"),
                rs.getInt("author_id")
        ));
    }

    public Optional<VacancyDto> getVacancyById(Integer vacancyId) {
        String sql = "SELECT * FROM vacancies WHERE id = ?";
        List<VacancyDto> result = jdbcTemplate.query(sql, new Object[]{vacancyId}, (rs, rowNum) -> new VacancyDto(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getDouble("salary"),
                rs.getInt("exp_from"),
                rs.getInt("exp_to"),
                rs.getBoolean("is_active"),
                rs.getInt("author_id")
        ));
        return result.stream().findFirst();
    }

    public List<VacancyDto> getVacanciesByCategory(Integer categoryId) {
        String sql = "SELECT * FROM vacancies WHERE category_id = ? AND is_active = true";
        return jdbcTemplate.query(sql, new Object[]{categoryId}, (rs, rowNum) -> new VacancyDto(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getDouble("salary"),
                rs.getInt("exp_from"),
                rs.getInt("exp_to"),
                rs.getBoolean("is_active"),
                rs.getInt("author_id")
        ));
    }

    public List<VacancyDto> getVacanciesUserRespondedTo(Integer userId) {
        String sql = """
        SELECT v.* FROM vacancies v
        JOIN responded_applicants ra ON v.id = ra.vacancy_id
        JOIN resumes r ON ra.resume_id = r.id
        WHERE r.applicant_id = ? AND v.is_active = true
    """;
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> new VacancyDto(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("category_id"),
                rs.getDouble("salary"),
                rs.getInt("exp_from"),
                rs.getInt("exp_to"),
                rs.getBoolean("is_active"),
                rs.getInt("author_id")
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

}
