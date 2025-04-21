package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.UserEditDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(UserDto userDto);

    Optional<UserDto> findUserByEmail(String email);

    boolean existsByEmail(String email);

    List<UserDto> findAllUsers();

    Optional<UserDto> getApplicantById(Integer userId);

    Optional<UserDto> getEmployeeById(Integer userId);

    Optional<UserDto> getUserById(Integer userId);

    Optional<UserDto> getUserById(String userId);

    void editUserProfile(Integer userId, UserDto userDto);

    Optional<List<UserDto>> getApplicantsForVacancy(Integer vacancyId);

    boolean authenticateUser(String email, String password);

    UserDto updateUserProfile(String email, UserEditDto userDto);

    UserEditDto fromDtoToUserEditDto(UserDto userDto);
}
