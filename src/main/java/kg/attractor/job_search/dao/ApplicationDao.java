package kg.attractor.job_search.dao;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.model.RespondedApplicant;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApplicationDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<RespondenApplicantDto> respondedApplicantRowMapper = (rs, rowNum) -> RespondenApplicantDto.builder()
            .id(rs.getInt("id"))
            .resumeId(rs.getInt("resume_id"))
            .vacancyId(rs.getInt("vacancy_id"))
            .confirmation(rs.getBoolean("confirmation"))
            .build();

    public RespondenApplicantDto saveResponse(Integer resumeId, Integer vacancyId) {
        String sql = "INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation) VALUES (?, ?, false) RETURNING *";
        return jdbcTemplate.queryForObject(sql, respondedApplicantRowMapper, resumeId, vacancyId);
    }

}
