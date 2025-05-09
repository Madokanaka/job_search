package kg.attractor.job_search.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacancyDto {
    private Integer id;

    @NotBlank(message = "{Vacancy.name.notBlank}")
    private String name;

    private String description;

    @Min(value = 1, message = "{Vacancy.categoryId.min}")
    @NotNull(message = "{Vacancy.categoryId.notNull}")
    private Integer categoryId;

    private String categoryName;

    @Positive(message = "{Vacancy.salary.positive}")
    @NotNull(message = "{Vacancy.salary.notNull}")
    private Double salary;

    @NotNull(message = "{Vacancy.expFrom.notNull}")
    @Min(value = 0, message = "{Vacancy.expFrom.min}")
    private Integer expFrom;

    @NotNull(message = "{Vacancy.expTo.notNull}")
    @Min(value = 0, message = "{Vacancy.expTo.min}")
    private Integer expTo;

    private Integer authorId;

    private LocalDateTime createdDate;
    private LocalDateTime updateTime;

    private Boolean isActive;
}
