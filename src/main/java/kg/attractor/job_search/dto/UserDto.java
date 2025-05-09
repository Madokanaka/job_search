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
public class UserDto {
    private Integer id;

    @NotBlank(message = "{User.name.notBlank}")
    @Size(max = 50, message = "{User.name.size}")
    private String name;

    @NotBlank(message = "{User.surname.notBlank}")
    @Size(max = 50, message = "{User.surname.size}")
    private String surname;

    @NotNull(message = "{User.age.notNull}")
    @Min(value = 18, message = "{User.age.min}")
    @Max(value = 100, message = "{User.age.max}")
    private Integer age;

    @NotBlank(message = "{User.email.notBlank}")
    @Email(message = "{User.email.invalid}")
    private String email;

    @NotBlank(message = "{User.password.notBlank}")
    @Size(min = 6, max = 25, message = "{User.password.size}")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).+$",
            message = "{User.password.pattern}"
    )
    private String password;

    @NotBlank(message = "{User.phoneNumber.notBlank}")
    @Pattern(
            regexp = "\\+?[0-9]{10,20}",
            message = "{User.phoneNumber.invalid}"
    )
    private String phoneNumber;

    private String avatar;

    @Pattern(
            regexp = "^(applicant|employer)$",
            message = "{User.accountType.invalid}"
    )
    private String accountType;
}
