package kg.attractor.job_search.dao;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

        List<Integer> categories = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("category_id"), resumeId);

        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Resume with id " + resumeId + " not found or category not set");
        }

        return categories.get(0);
    }

    public int getCategoryByVacancyId(Integer vacancyId) {
        String sql = "SELECT category_id FROM VACANCIES WHERE id = ?";

        List<Integer> categories = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("category_id"), vacancyId);

        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Vacancy with id " + vacancyId + " not found or category not set");
        }

        return categories.get(0);
    }


    public RespondenApplicantDto findResponseByResumeAndVacancy(Integer resumeId, Integer vacancyId) {
        String sql = "SELECT id, resume_id, vacancy_id, confirmation FROM responded_applicants WHERE resume_id = ? AND vacancy_id = ?";
        List<RespondenApplicantDto> responses = jdbcTemplate.query(sql, (rs, rowNum) -> {
            RespondenApplicantDto dto = new RespondenApplicantDto();
            dto.setId(rs.getInt("id"));
            dto.setResumeId(rs.getInt("resume_id"));
            dto.setVacancyId(rs.getInt("vacancy_id"));
            dto.setConfirmation(rs.getBoolean("confirmation"));
            return dto;
        }, resumeId, vacancyId);

        if (responses.isEmpty()) {
            return null;
        }

        return responses.get(0);
    }

}
