package kg.attractor.job_search.service.impl;


import kg.attractor.job_search.dto.UserPictureDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.model.UserPicture;
import kg.attractor.job_search.repository.UserPictureRepository;
import kg.attractor.job_search.service.ImageService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.userdetails.User;


@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final UserPictureRepository userPictureRepository;
    private final FileUtil fileUtil;
    private final UserService userService;

    @Override
    public String saveImage(UserPictureDto dto) {
        log.info("Saving image for userId={}", dto.getUserId());

        String fileName = fileUtil.saveUploadFile(dto.getFile(), "images/");
        UserPicture userPicture = new UserPicture();
        userPicture.setUser(userService.getUserModelById(Math.toIntExact(dto.getUserId())));
        userPicture.setFileName(fileName);
        userPictureRepository.save(userPicture);

        log.debug("Image saved with fileName={}", fileName);
        return fileName;
    }

    @Override
    public ResponseEntity<?> findByName(String fileName) {
        log.info("Fetching image by fileName={}", fileName);
        return fileUtil.getOutputFile(fileName, "images/", MediaType.IMAGE_JPEG);
    }

    @Override
    public ResponseEntity<?> downloadImage(long imageId) {
        log.info("Downloading image with imageId={}", imageId);

        UserPicture image = userPictureRepository.findById(imageId)
                .orElseThrow(() -> {
                    log.warn("Image not found for imageId={}", imageId);
                    return new ResourceNotFoundException("Image was not found");
                });

        String fileName = image.getFileName();
        log.debug("Found image fileName={} for imageId={}", fileName, imageId);

        return fileUtil.getOutputFile(fileName, "images/", MediaType.IMAGE_JPEG);
    }

    @Override
    public void uploadImage(User principal, MultipartFile file) {
        Long userId = Integer.toUnsignedLong(userService.findUserModelByEmail(principal.getUsername()).getId());
        log.info("Saving image for userId={}", userId);

        String fileName = fileUtil.saveUploadFile(file, "images/");
        UserPicture userPicture = new UserPicture();
        userPicture.setUser(userService.getUserModelById(Math.toIntExact(userId)));
        userPicture.setFileName(fileName);
        userPictureRepository.save(userPicture);
        userService.saveAvatar(userId, fileName);

        log.debug("Image saved with fileName={}", fileName);
    }
}
