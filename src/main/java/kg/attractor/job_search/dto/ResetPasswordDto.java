package kg.attractor.job_search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @NotBlank(message = "{reset.password.token.notBlank}")
    private String token;

    @NotBlank(message = "{reset.password.password.notBlank}")
    @Size(min = 6, max = 25, message = "{reset.password.password.size}")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]+$",
            message = "{reset.password.password.pattern}")
    private String password;
}