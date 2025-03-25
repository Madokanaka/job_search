package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(UserDto userDto);

    String uploadAvatar(MultipartFile file);

    Optional<UserDto> findUserByEmail(String email);

    boolean existsByEmail(String email);

    List<UserDto> findAllUsers();

    Optional<UserDto> getApplicantById(Integer userId);

    Optional<UserDto> getEmployeeById(Integer userId);
}
