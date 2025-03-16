package kg.attractor.job_search.dto;

import java.util.List;

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
