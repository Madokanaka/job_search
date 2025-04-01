package kg.attractor.job_search.dao;

import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.WorkExperienceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ContactInfoDao {

    private final JdbcTemplate jdbcTemplate;

    public void createContactInfo(ContactInfo contactInfoDto) {
        String sql = "INSERT INTO contact_info (resume_id, type_id, `value`) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, contactInfoDto.getResumeId(), contactInfoDto.getTypeId(), contactInfoDto.getValue());
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

    public boolean existsTypeById(Integer typeId) {
        String sql = "SELECT COUNT(*) FROM contact_types WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, typeId);
        return count != null && count > 0;
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
