package kg.attractor.job_search.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationInfoDto {
    private String institution;
    private String program;
    private LocalDate startDate;
    private LocalDate endDate;
    private String degree;
}

