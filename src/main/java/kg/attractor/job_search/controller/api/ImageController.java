package kg.attractor.job_search.controller.api;

import kg.attractor.job_search.dto.UserPictureDto;
import kg.attractor.job_search.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping({"{imageName}"})
    public ResponseEntity<?> getImage(@PathVariable("imageName") String imageName) {
        return imageService.findByName(imageName);
    }

    @PostMapping
    public String uploadImage(@ModelAttribute UserPictureDto dto) {
        return imageService.saveImage(dto);
    }

}
