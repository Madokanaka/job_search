package kg.attractor.job_search.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotBlank(message = "Resume name is required")
    private String name;

    @Min(value = 1, message = "Category must be a positive number")
    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @Positive(message = "Salary must be a positive number")
    @NotNull(message = "Salary amount is required")
    private Double salary;

    private Boolean isActive;

    @Valid
    private List<EducationInfoDto> educationInfoList;

    @Valid
    private List<WorkExperienceInfoDto> workExperienceInfoList;

    @Valid
    private List<ContactInfoDto> contactInfoList;
}
