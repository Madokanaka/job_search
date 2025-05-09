package kg.attractor.job_search.dto;

import java.time.LocalDate;

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
public class EducationInfoDto {
    @NotBlank(message = "{education.info.institution.notBlank}")
    private String institution;

    @NotBlank(message = "{education.info.program.notBlank}")
    private String program;

    @NotNull(message = "{education.info.startDate.notNull}")
    private LocalDate startDate;

    @NotNull(message = "{education.info.endDate.notNull}")
    private LocalDate endDate;

    @NotBlank(message = "{education.info.degree.notBlank}")
    private String degree;
}