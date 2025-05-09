package kg.attractor.job_search.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceInfoDto {
    @Min(value = 0, message = "{WorkExperienceInfo.years.min}")
    @NotNull(message = "{WorkExperienceInfo.years.notNull}")
    private Integer years;

    @NotBlank(message = "{WorkExperienceInfo.companyName.notBlank}")
    private String companyName;

    @NotBlank(message = "{WorkExperienceInfo.position.notBlank}")
    private String position;

    private String responsibilities;
}
