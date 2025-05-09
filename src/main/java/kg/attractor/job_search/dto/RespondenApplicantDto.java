package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "{respondent.applicant.resumeId.notBlank}")
    @Positive(message = "{respondent.applicant.resumeId.positive}")
    private Integer resumeId;

    @NotBlank(message = "{respondent.applicant.vacancyId.notBlank}")
    @Positive(message = "{respondent.applicant.vacancyId.positive}")
    private Integer vacancyId;

    private Boolean confirmation;
}