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

    @NotBlank(message = "{resume.name.notBlank}")
    private String name;

    @NotNull(message = "{resume.categoryId.notNull}")
    @Min(value = 1, message = "{resume.categoryId.min}")
    private Integer categoryId;

    private String categoryName;

    @NotNull(message = "{resume.salary.notNull}")
    @Positive(message = "{resume.salary.positive}")
    private Double salary;

    private Boolean isActive;

    private LocalDateTime created_date;
    private LocalDateTime update_time;

    private List<EducationInfoDto> educationInfoList = new ArrayList<>();

    private List<WorkExperienceInfoDto> workExperienceInfoList = new ArrayList<>();

    @Email(message = "{resume.contactEmail.email}")
    private String contactEmail;

    @Pattern(regexp = "\\+?[0-9]{10,20}", message = "{resume.phoneNumber.pattern}")
    private String phoneNumber;

    private String linkedIn;

    private String telegram;

    private String facebook;
}