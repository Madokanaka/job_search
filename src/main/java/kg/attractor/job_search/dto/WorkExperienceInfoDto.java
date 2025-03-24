package kg.attractor.job_search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceInfoDto {
    private Integer years;
    private String companyName;
    private String position;
    private String responsibilities;
}

