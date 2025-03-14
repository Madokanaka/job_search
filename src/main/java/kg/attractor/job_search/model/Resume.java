package kg.attractor.job_search.model;

import java.time.LocalDateTime;

public class Resume {
    private Integer id;
    private Integer applicantId;
    private String name;
    private Integer categoryId;
    private Double salary;
    private Boolean isActive;
    private LocalDateTime created_date;
    private LocalDateTime update_time;
}
