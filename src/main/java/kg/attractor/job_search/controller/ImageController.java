package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.UserPictureDto;
import kg.attractor.job_search.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping({"imageName"})
    public ResponseEntity<?> getImage(@RequestParam("imageName") String imageName) {
        return imageService.findByName(imageName);
    }

    @PostMapping
    public String uploadImage(UserPictureDto dto) {
        return imageService.saveImage(dto);
    }

}
