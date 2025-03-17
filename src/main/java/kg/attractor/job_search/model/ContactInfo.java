package kg.attractor.job_search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo {
    private Integer id;
    private Integer typeId;
    private Integer resumeId;
    private String value;
}
