package kg.attractor.job_search.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {
    private Integer id;
    @NotBlank(message = "Имя резюме необходимо")
    private String name;

    @NotNull(message = "ID категории необходим")
    @Min(value = 1, message = "Категория должна быть положительной")
    private Integer categoryId;
    @NotBlank(message = "Category name cannot be empty")
    private String categoryName;

    @Positive(message = "Зарплата должна быть положительной")
    @NotNull(message = "Поле зарплаты необходимо")
    private Double salary;

    private Boolean isActive;

    private LocalDateTime created_date;
    private LocalDateTime update_time;

    @Valid
    @Builder.Default

    private List<EducationInfoDto> educationInfoList = new ArrayList<>();

    @Valid
    @Builder.Default

    private List<WorkExperienceInfoDto> workExperienceInfoList = new ArrayList<>();

    @Email(message = "email должен быть правильного формата")
    private String contactEmail;

    @Pattern(regexp = "\\+?[0-9]{10,20}", message = "Invalid phone number format")
    private String phoneNumber;

    private String linkedIn;

    private String telegram;

    private String facebook;
}
