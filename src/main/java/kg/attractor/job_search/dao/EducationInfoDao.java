package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.model.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
}
