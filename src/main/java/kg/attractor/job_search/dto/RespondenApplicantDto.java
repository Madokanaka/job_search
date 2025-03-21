package kg.attractor.job_search.dto;

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
    private Integer resumeId;
    private Integer vacancyId;
    private Boolean confirmation;
}
