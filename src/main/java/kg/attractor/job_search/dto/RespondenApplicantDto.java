package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespondenApplicantDto {
    private Integer id;

    @NotBlank(message = "Resume ID is required")
    @Positive(message = "Resume ID must be positive")
    private Integer resumeId;

    @NotBlank(message = "Vacancy ID is required")
    @Positive(message = "Vacancy ID must be positive")
    private Integer vacancyId;

    private Boolean confirmation;
}
