package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.UserEditDto;
import kg.attractor.job_search.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(UserDto userDto);

    Optional<UserDto> findUserByEmail(String email);

    User findUserModelByEmail(String email);

    boolean existsByEmail(String email);

    List<UserDto> findAllUsers();

    Optional<UserDto> getApplicantById(Integer userId);

    Optional<UserDto> getEmployeeById(Integer userId);

    Optional<UserDto> getUserById(Integer userId);

    User getUserModelById(Integer userId);

    Optional<UserDto> getUserById(String userId);

    void editUserProfile(Integer userId, UserDto userDto);

    Optional<List<UserDto>> getApplicantsForVacancy(Integer vacancyId);

    boolean authenticateUser(String email, String password);

    UserDto updateUserProfile(String email, UserEditDto userDto);

    UserEditDto fromDtoToUserEditDto(UserDto userDto);

    Page<UserDto> getEmployers(String pageNumber, String pageSize);

    Optional<User> findById(Integer id);

    void saveAvatar(Long userId, String fileName);
}
