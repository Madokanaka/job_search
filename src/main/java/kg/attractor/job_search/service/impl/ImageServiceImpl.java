package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserDao;
import kg.attractor.job_search.dao.UserPictureDao;
import kg.attractor.job_search.dto.UserPictureDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.model.UserPicture;
import kg.attractor.job_search.service.ImageService;
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

    private final UserPictureDao userPictureDao;
    private final FileUtil fileUtil;
    private final UserDao userDao;

    @Override
    public String saveImage(UserPictureDto dto) {
        log.info("Saving image for userId={}", dto.getUserId());

        String fileName = fileUtil.saveUploadFile(dto.getFile(), "images/");
        userPictureDao.save(dto.getUserId(), fileName);
        userDao.saveAvatar(dto.getUserId(), fileName);

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

        UserPicture image = userPictureDao.getImageById(imageId)
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
        Long userId = Integer.toUnsignedLong(userDao.findByEmail(principal.getUsername()).getId());
        log.info("Saving image for userId={}", userId);

        String fileName = fileUtil.saveUploadFile(file, "images/");

        userPictureDao.save(userId, fileName);
        userDao.saveAvatar(userId, fileName);

        log.debug("Image saved with fileName={}", fileName);
    }
}
