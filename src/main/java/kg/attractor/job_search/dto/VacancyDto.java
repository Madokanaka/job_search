package kg.attractor.job_search.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotBlank(message = "Vacancy name cannot be empty")
    private String name;

    private String description;

    @Min(value = 1, message = "Category must be a positive number")
    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    private String categoryName;

    @Positive(message = "Salary must be a positive number")
    @NotNull(message = "Salary amount is required")
    private Double salary;

    @NotNull(message = "Experience from is required")
    @Min(value = 0, message = "Experience from must be at least 0")
    private Integer expFrom;

    @NotNull(message = "Experience to is required")
    @Min(value = 0, message = "Experience to must be at least 0")
    private Integer expTo;

    public Integer authorId;

    private LocalDateTime createdDate;
    private LocalDateTime updateTime;

    private Boolean isActive;
}
