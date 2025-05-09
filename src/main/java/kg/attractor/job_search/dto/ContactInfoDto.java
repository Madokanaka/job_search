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
public class ContactInfoDto {
    @NotNull(message = "{contact.info.typeId.notNull}")
    @Min(value = 1, message = "{contact.info.typeId.min}")
    private Integer typeId;

    @NotBlank(message = "{contact.info.value.notBlank}")
    private String value;
}