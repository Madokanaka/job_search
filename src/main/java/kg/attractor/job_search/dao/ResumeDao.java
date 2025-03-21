package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.Resume;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ResumeDao {

    private final JdbcTemplate jdbcTemplate;

    public ResumeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createResume(Resume resume) {
        String sql = "INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, resume.getApplicantId(), resume.getName(), resume.getCategoryId(),
                resume.getSalary(), resume.getIsActive(), resume.getCreated_date(), resume.getUpdate_time());
    }

    public boolean updateResume(Resume resume) {
        String sql = "UPDATE resumes SET name = ?, category_id = ?, salary = ?, is_active = ?, update_time = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, resume.getName(), resume.getCategoryId(), resume.getSalary(),
                resume.getIsActive(), LocalDateTime.now(), resume.getId());

        return rowsAffected > 0;
    }

    public boolean deleteResume(Integer resumeId) {
        String sql = "DELETE FROM resumes WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, resumeId);

        return rowsAffected > 0;
    }

    public Optional<Resume> findById(Integer resumeId) {
        String sql = "SELECT * FROM resumes WHERE id = ?";
        List<Resume> resumes = jdbcTemplate.query(sql, new Object[]{resumeId}, (rs, rowNum) -> {
            Resume resume = new Resume();
            resume.setId(rs.getInt("id"));
            resume.setApplicantId(rs.getInt("applicant_id"));
            resume.setName(rs.getString("name"));
            resume.setCategoryId(rs.getInt("category_id"));
            resume.setSalary(rs.getDouble("salary"));
            resume.setIsActive(rs.getBoolean("is_active"));
            resume.setCreated_date(rs.getTimestamp("created_date").toLocalDateTime());
            resume.setUpdate_time(rs.getTimestamp("update_time").toLocalDateTime());
            return resume;
        });
        return resumes.isEmpty() ? Optional.empty() : Optional.of(resumes.get(0));
    }

    public List<Resume> findAll() {
        String sql = "SELECT * FROM resumes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Resume resume = new Resume();
            resume.setId(rs.getInt("id"));
            resume.setApplicantId(rs.getInt("applicant_id"));
            resume.setName(rs.getString("name"));
            resume.setCategoryId(rs.getInt("category_id"));
            resume.setSalary(rs.getDouble("salary"));
            resume.setIsActive(rs.getBoolean("is_active"));
            resume.setCreated_date(rs.getTimestamp("created_date").toLocalDateTime());
            resume.setUpdate_time(rs.getTimestamp("update_time").toLocalDateTime());
            return resume;
        });
    }

    public Optional<List<Resume>> findByUserId(Integer userId) {
        String sql = "SELECT * FROM resumes WHERE applicant_id = ?";
        List<Resume> resumes = jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            Resume resume = new Resume();
            resume.setId(rs.getInt("id"));
            resume.setApplicantId(rs.getInt("applicant_id"));
            resume.setName(rs.getString("name"));
            resume.setCategoryId(rs.getInt("category_id"));
            resume.setSalary(rs.getDouble("salary"));
            resume.setIsActive(rs.getBoolean("is_active"));
            resume.setCreated_date(rs.getTimestamp("created_date").toLocalDateTime());
            resume.setUpdate_time(rs.getTimestamp("update_time").toLocalDateTime());
            return resume;
        });
        return resumes.isEmpty() ? Optional.empty() : Optional.of(resumes);
    }

}
