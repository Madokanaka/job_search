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
    private String name;
    private Integer categoryId;
    private Double salary;
    private Boolean isActive;

    private List<EducationInfoDto> educationInfoList;
    private List<WorkExperienceInfoDto> workExperienceInfoList;
    private List<ContactInfoDto> contactInfoList;
}
