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
public class ContactInfoDto {
    @NotBlank(message = "Contact type ID is required")
    @Positive(message = "Type ID must be positive")
    private Integer typeId;

    @NotBlank(message = "Contact value is required")
    private String value;
}
