package kg.attractor.job_search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {
    private Integer id;
    private String name;
    private Integer categoryId;
    private Double salary;
    private Boolean isActive;

    private List<WorkExperienceInfoDto> workExperiences;
    private List<EducationInfoDto> educationInfos;
    private List<ContactInfoDto> contactInfos;
}
