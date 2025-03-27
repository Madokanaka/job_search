package kg.attractor.job_search.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceInfoDto {
    @Min(value = 0, message = "Years of experience cannot be negative")
    @NotNull(message = "Years of experience field is required")
    private Integer years;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Position is required")
    private String position;

    private String responsibilities;
}
