package kg.attractor.job_search.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class UserPictureDto {
    private MultipartFile file;
    private Long userId;
}
