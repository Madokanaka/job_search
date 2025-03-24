package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.WorkExperienceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ResumeDao {

    private final JdbcTemplate jdbcTemplate;

    public int createResume(Resume resume) {
        String sql = "INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, resume.getApplicantId(), resume.getName(), resume.getCategoryId(),
                resume.getSalary(), resume.getIsActive(), resume.getCreated_date(), resume.getUpdate_time());
    }

    public boolean updateResume(Resume resume) {
        String sql = "UPDATE resumes SET name = ?, salary = ?, is_active = ?, update_time = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, resume.getName(), resume.getSalary(),
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

    public void createContactInfo(ContactInfo contactInfo) {
        String sql = "INSERT INTO contact_info (resume_id, type_id, `value`) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, contactInfo.getResumeId(), contactInfo.getTypeId(), contactInfo.getValue());
    }

    public void deleteContactInfoByResumeId(Integer resumeId) {
        String sql = "DELETE FROM contact_info WHERE resume_id = ?";
        jdbcTemplate.update(sql, resumeId);
    }

    public List<ContactInfo> findContactInfoByResumeId(Integer resumeId) {
        String sql = "SELECT * FROM contact_info WHERE resume_id = ?";
        return jdbcTemplate.query(sql, new Object[]{resumeId}, (rs, rowNum) -> {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setId(rs.getInt("id"));
            contactInfo.setResumeId(rs.getInt("resume_id"));
            contactInfo.setTypeId(rs.getInt("type_id"));
            contactInfo.setValue(rs.getString("value"));
            return contactInfo;
        });
    }

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

    public void createWorkExperienceInfo(WorkExperienceInfo workExperienceInfo) {
        String sql = "INSERT INTO work_experience (resume_id, years, company_name, position, responsibilities) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, workExperienceInfo.getResumeId(), workExperienceInfo.getYears(),
                workExperienceInfo.getCompanyName(), workExperienceInfo.getPosition(), workExperienceInfo.getResponsibilities());
    }

    public void deleteWorkExperienceInfoByResumeId(Integer resumeId) {
        String sql = "DELETE FROM work_experience WHERE resume_id = ?";
        jdbcTemplate.update(sql, resumeId);
    }

    public List<WorkExperienceInfo> findWorkExperienceInfoByResumeId(Integer resumeId) {
        String sql = "SELECT * FROM work_experience WHERE resume_id = ?";
        return jdbcTemplate.query(sql, new Object[]{resumeId}, (rs, rowNum) -> {
            WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
            workExperienceInfo.setId(rs.getInt("id"));
            workExperienceInfo.setResumeId(rs.getInt("resume_id"));
            workExperienceInfo.setYears(rs.getInt("years"));
            workExperienceInfo.setCompanyName(rs.getString("company_name"));
            workExperienceInfo.setPosition(rs.getString("position"));
            workExperienceInfo.setResponsibilities(rs.getString("responsibilities"));
            return workExperienceInfo;
        });
    }

    public boolean existsApplicantById(Integer userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    public boolean existsCategoryById(Integer categoryId) {
        String sql = "SELECT COUNT(*) FROM categories WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
        return count != null && count > 0;
    }

    public boolean existsTypeById(Integer typeId) {
        String sql = "SELECT COUNT(*) FROM contact_types WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, typeId);
        return count != null && count > 0;
    }
}
