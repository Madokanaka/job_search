package kg.attractor.job_search.dao;

import kg.attractor.job_search.dto.RespondenApplicantDto;
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
}
