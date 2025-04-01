package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.EducationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EducationInfoDao {

    private final JdbcTemplate jdbcTemplate;

    public void createEducationInfo(EducationInfo educationInfo) {
        String sql = "INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, educationInfo.getResumeId(), educationInfo.getInstitution(), educationInfo.getProgram(),
                educationInfo.getStartDate(), educationInfo.getEndDate(), educationInfo.getDegree());
    }

    public void deleteEducationInfoByResumeId(Integer resumeId) {
        String sql = "DELETE FROM education_info WHERE resume_id = ?";
        jdbcTemplate.update(sql, resumeId);
    }

    public List<EducationInfo> findEducationInfoByResumeId(Integer resumeId) {
        String sql = "SELECT * FROM education_info WHERE resume_id = ?";
        return jdbcTemplate.query(sql, new Object[]{resumeId}, (rs, rowNum) -> {
            EducationInfo educationInfo = new EducationInfo();
            educationInfo.setId(rs.getInt("id"));
            educationInfo.setResumeId(rs.getInt("resume_id"));
            educationInfo.setInstitution(rs.getString("institution"));
            educationInfo.setProgram(rs.getString("program"));
            educationInfo.setStartDate(rs.getDate("start_date").toLocalDate());
            educationInfo.setEndDate(rs.getDate("end_date").toLocalDate());
            educationInfo.setDegree(rs.getString("degree"));
            return educationInfo;
        });
    }
}
