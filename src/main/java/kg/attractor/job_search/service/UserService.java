package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    UserDto registerUser(UserDto userDto);

    Optional<User> login(String email, String password);

    String uploadAvatar(Integer userId, MultipartFile file);
}
