package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserDao;
import kg.attractor.job_search.exception.DatabaseOperationException;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.exception.TypeException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;


    @Override
    public void registerUser(UserDto userDto) {
        if (userDao.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAvatar(userDto.getAvatar());
        if (!("employer".equals(userDto.getAccountType()) || "applicant".equals(userDto.getAccountType()))) {
            throw new TypeException("User should be either employer or applicant");
        }
        user.setAccountType(userDto.getAccountType());
        user.setPassword(userDto.getPassword());

        int rowsAffected = userDao.createUser(user);
        if (rowsAffected == 0) {
            throw new RuntimeException("Error creating user");
        }
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        return Optional.ofNullable(userDao.findByEmail(email))
                .map(user -> UserDto.builder()
                        .name(user.getName())
                        .surname(user.getSurname())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .phoneNumber(user.getPhoneNumber())
                        .avatar(user.getAvatar())
                        .accountType(user.getAccountType())
                        .build());
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userDao.findAll();

        return users.stream()
                .map(user -> UserDto.builder()
                        .name(user.getName())
                        .surname(user.getSurname())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .phoneNumber(user.getPhoneNumber())
                        .avatar(user.getAvatar())
                        .accountType(user.getAccountType())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getApplicantById(Integer userId) {
        User user = userDao.findById(userId);

        if (user == null || !"APPLICANT".equalsIgnoreCase(user.getAccountType())) {
            throw new ResourceNotFoundException("Could not find applicant with id " + userId);
        }

        return Optional.of(UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .accountType(user.getAccountType())
                .build());
    }

    @Override
    public Optional<UserDto> getEmployeeById(Integer userId) {
        User user = userDao.findById(userId);

        if (user == null || !"EMPLOYER".equalsIgnoreCase(user.getAccountType())) {
            throw new ResourceNotFoundException("Could not find employee with id " + userId);
        }

        return Optional.of(UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .accountType(user.getAccountType())
                .build());
    }

    @Override
    public Optional<UserDto> getUserById(Integer userId) {
        User user = userDao.findById(userId);

        if (user == null) {
            throw new ResourceNotFoundException("Could not find user with id " + userId);
        }

        return Optional.of(UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .accountType(user.getAccountType())
                .build());
    }

    @Override
    public void editUserProfile(Integer userId, UserDto userDto) {
        User existingUser = userDao.findById(userId);
        if (existingUser == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (userDto.getName() == null || userDto.getName().trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }

        if (userDto.getSurname() == null || userDto.getSurname().trim().isEmpty()) {
            throw new RuntimeException("Surname cannot be empty");
        }

        if (userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email cannot be empty");
        }

        if (userDto.getPhoneNumber() == null || userDto.getPhoneNumber().trim().isEmpty()) {
            throw new RuntimeException("Phone number cannot be empty");
        }

        if (userDto.getAccountType() == null || userDto.getAccountType().trim().isEmpty()) {
            throw new RuntimeException("Account type cannot be empty");
        }

        if (!("employer".equals(userDto.getAccountType()) || "applicant".equals(userDto.getAccountType()))) {
            throw new TypeException("User should be either employer or applicant");
        }

        existingUser.setName(userDto.getName());
        existingUser.setAge(userDto.getAge());
        existingUser.setSurname(userDto.getSurname());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setAvatar(userDto.getAvatar());
        existingUser.setAccountType(userDto.getAccountType());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(userDto.getPassword());
        }

        int rowsAffected = userDao.updateUser(existingUser);
        if (rowsAffected == 0) {
            throw new DatabaseOperationException("Error updating user profile");
        }
    }

    @Override
    public Optional<List<UserDto>> getApplicantsForVacancy(Integer vacancyId) {
        List<User> applicants = userDao.getApplicantsForVacancy(vacancyId);

        List<UserDto> userDtos = applicants.stream()
                .map(user -> new UserDto(
                        user.getName(),
                        user.getSurname(),
                        user.getAge(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getPhoneNumber(),
                        user.getAvatar(),
                        user.getAccountType()
                ))
                .collect(Collectors.toList());

        return userDtos.isEmpty() ? Optional.empty() : Optional.of(userDtos);
    }

}
