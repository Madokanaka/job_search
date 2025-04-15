package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.UserPictureDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String saveImage(UserPictureDto dto);

    ResponseEntity<?> findByName(String fileName);

    ResponseEntity<?> downloadImage(long imageId);

    void uploadImage(User principal, MultipartFile file);
}
