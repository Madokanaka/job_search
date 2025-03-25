package kg.attractor.job_search.dao;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApplicationDao {

    private final JdbcTemplate jdbcTemplate;


    public RespondenApplicantDto saveResponse(Integer resumeId, Integer vacancyId) {
        String sql = "INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation) VALUES (?, ?, false)";
        int rowsAffected = jdbcTemplate.update(sql, resumeId, vacancyId);
        if (rowsAffected > 0) {
            String selectSql = "SELECT id, resume_id, vacancy_id, confirmation FROM responded_applicants WHERE resume_id = ? AND vacancy_id = ?";
            return jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
                RespondenApplicantDto dto = new RespondenApplicantDto();
                dto.setId(rs.getInt("id"));
                dto.setResumeId(rs.getInt("resume_id"));
                dto.setVacancyId(rs.getInt("vacancy_id"));
                dto.setConfirmation(rs.getBoolean("confirmation"));
                return dto;
            }, resumeId, vacancyId);
        }
        return null;
    }

    public int getCategoryByResumeId(Integer resumeId) {
        String sql = "SELECT category_id FROM RESUMES WHERE id = ?";
        Integer category = jdbcTemplate.queryForObject(sql, Integer.class, resumeId);

        if (category == null) {
            throw new ResourceNotFoundException("Resume with id " + resumeId + " not found or category not set");
        }

        return category;
    }

    public int getCategoryByVacancyId(Integer vacancyId) {
        String sql = "SELECT category_id FROM VACANCIES WHERE id = ?";
        Integer category = jdbcTemplate.queryForObject(sql, Integer.class, vacancyId);

        if (category == null) {
            throw new ResourceNotFoundException("Resume with id " + vacancyId + " not found or category not set");
        }

        return category;
    }

    public RespondenApplicantDto findResponseByResumeAndVacancy(Integer resumeId, Integer vacancyId) {
        String sql = "SELECT id, resume_id, vacancy_id, confirmation FROM responded_applicants WHERE resume_id = ? AND vacancy_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            RespondenApplicantDto dto = new RespondenApplicantDto();
            dto.setId(rs.getInt("id"));
            dto.setResumeId(rs.getInt("resume_id"));
            dto.setVacancyId(rs.getInt("vacancy_id"));
            dto.setConfirmation(rs.getBoolean("confirmation"));
            return dto;
        }, resumeId, vacancyId);
    }
}
