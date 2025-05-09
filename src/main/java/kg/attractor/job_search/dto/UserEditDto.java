package kg.attractor.job_search.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDto {
    @NotBlank(message = "{UserEdit.name.notBlank}")
    @Size(max = 50, message = "{UserEdit.name.size}")
    private String name;

    @NotBlank(message = "{UserEdit.surname.notBlank}")
    @Size(max = 50, message = "{UserEdit.surname.size}")
    private String surname;

    @NotNull(message = "{UserEdit.age.notNull}")
    @Min(value = 18, message = "{UserEdit.age.min}")
    @Max(value = 100, message = "{UserEdit.age.max}")
    private Integer age;

    @NotBlank(message = "{UserEdit.phoneNumber.notBlank}")
    @Pattern(regexp = "\\+?[0-9]{10,20}", message = "{UserEdit.phoneNumber.invalid}")
    private String phoneNumber;
}
