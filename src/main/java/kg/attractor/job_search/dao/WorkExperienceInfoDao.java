//package kg.attractor.job_search.dao;
//
//import kg.attractor.job_search.model.Resume;
//import kg.attractor.job_search.model.WorkExperienceInfo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class WorkExperienceInfoDao {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public void createWorkExperienceInfo(WorkExperienceInfo workExperienceInfo) {
//        String sql = "INSERT INTO work_experience (resume_id, years, company_name, position, responsibilities) VALUES (?, ?, ?, ?, ?)";
//        jdbcTemplate.update(sql, workExperienceInfo.getResumeId(), workExperienceInfo.getYears(),
//                workExperienceInfo.getCompanyName(), workExperienceInfo.getPosition(), workExperienceInfo.getResponsibilities());
//    }
//
//    public void deleteWorkExperienceInfoByResumeId(Integer resumeId) {
//        String sql = "DELETE FROM work_experience WHERE resume_id = ?";
//        jdbcTemplate.update(sql, resumeId);
//    }
//
//    public List<WorkExperienceInfo> findWorkExperienceInfoByResumeId(Integer resumeId) {
//        String sql = "SELECT * FROM work_experience WHERE resume_id = ?";
//        return jdbcTemplate.query(sql, new Object[]{resumeId}, (rs, rowNum) -> {
//            WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
//            workExperienceInfo.setId(rs.getInt("id"));
//            workExperienceInfo.setResumeId(rs.getInt("resume_id"));
//            workExperienceInfo.setYears(rs.getInt("years"));
//            workExperienceInfo.setCompanyName(rs.getString("company_name"));
//            workExperienceInfo.setPosition(rs.getString("position"));
//            workExperienceInfo.setResponsibilities(rs.getString("responsibilities"));
//            return workExperienceInfo;
//        });
//    }
//
//    public Optional<Resume> findById(Integer resumeId) {
//        String sql = "SELECT * FROM resumes WHERE id = ?";
//        List<Resume> resumes = jdbcTemplate.query(sql, new Object[]{resumeId}, (rs, rowNum) -> {
//            Resume resume = new Resume();
//            resume.setId(rs.getInt("id"));
//            resume.setApplicantId(rs.getInt("applicant_id"));
//            resume.setName(rs.getString("name"));
//            resume.setCategoryId(rs.getInt("category_id"));
//            resume.setSalary(rs.getDouble("salary"));
//            resume.setIsActive(rs.getBoolean("is_active"));
//            resume.setCreated_date(rs.getTimestamp("created_date").toLocalDateTime());
//            resume.setUpdate_time(rs.getTimestamp("update_time").toLocalDateTime());
//            return resume;
//        });
//        return resumes.isEmpty() ? Optional.empty() : Optional.of(resumes.get(0));
//    }
//}
