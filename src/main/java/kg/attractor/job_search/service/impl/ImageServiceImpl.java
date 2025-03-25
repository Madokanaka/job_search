package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserPictureDao;
import kg.attractor.job_search.dto.UserPictureDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.model.UserPicture;
import kg.attractor.job_search.service.ImageService;
import kg.attractor.job_search.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final UserPictureDao userPictureDao;
    private final FileUtil fileUtil;

    @Override
    public String saveImage(UserPictureDto dto) {

        String fileName =  fileUtil.saveUploadFile(dto.getFile(), "images/");
        userPictureDao.save(dto.getUserId(), fileName);
        return fileName;
    }

    @Override
    public ResponseEntity<?> findByName(String fileName) {
        return fileUtil.getOutputFile(fileName, "images/", MediaType.IMAGE_JPEG);
    }

    @Override
    public ResponseEntity<?> downloadImage(long imageId) {
        UserPicture image = userPictureDao.getImageById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image was not found"));
        String fileName = image.getFileName();
        return fileUtil.getOutputFile(fileName, "images/", MediaType.IMAGE_JPEG);
    }
}
