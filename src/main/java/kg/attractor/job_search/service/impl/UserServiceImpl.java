package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserDao;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.DatabaseOperationException;
import kg.attractor.job_search.exception.RecordAlreadyExistsException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDto userDto) {
        log.info("Attempting to register user with email: {}", userDto.getEmail());

        if (userDao.existsByEmail(userDto.getEmail())) {
            log.warn("Registration failed: email {} already exists", userDto.getEmail());
            throw new RecordAlreadyExistsException("User with this email already exists");
        }

        if (!("employer".equals(userDto.getAccountType()) || "applicant".equals(userDto.getAccountType()))) {
            log.error("Invalid account type: {}", userDto.getAccountType());
            throw new BadRequestException("User role should be either employer or applicant");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAvatar(userDto.getAvatar());
        user.setAccountType(userDto.getAccountType().toLowerCase());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        int rowsAffected = userDao.createUser(user);
        if (rowsAffected == 0) {
            log.error("User creation failed for email: {}", userDto.getEmail());
            throw new DatabaseOperationException("Error creating user");
        }

        log.info("User registered successfully: {}", userDto.getEmail());
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        User user = userDao.findByEmail(email);
        if (user == null) {
            log.warn("User with email {} not found", email);
            throw new UserNotFoundException("User with this email does not exist");
        }
        return Optional.of(convertToDto(user));
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if email exists: {}", email);
        return userDao.existsByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        log.info("Fetching all users");
        return userDao.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getApplicantById(Integer userId) {
        log.debug("Fetching applicant with ID: {}", userId);
        User user = userDao.findById(userId);
        if (user == null || !"APPLICANT".equalsIgnoreCase(user.getAccountType())) {
            log.warn("Applicant not found or invalid type for ID: {}", userId);
            throw new UserNotFoundException("Could not find applicant with id " + userId);
        }
        return Optional.of(convertToDto(user));
    }

    @Override
    public Optional<UserDto> getEmployeeById(Integer userId) {
        log.debug("Fetching employee with ID: {}", userId);
        User user = userDao.findById(userId);
        if (user == null || !"EMPLOYER".equalsIgnoreCase(user.getAccountType())) {
            log.warn("Employer not found or invalid type for ID: {}", userId);
            throw new UserNotFoundException("Could not find employee with id " + userId);
        }
        return Optional.of(convertToDto(user));
    }

    @Override
    public Optional<UserDto> getUserById(Integer userId) {
        log.debug("Fetching user with ID: {}", userId);
        User user = userDao.findById(userId);
        if (user == null) {
            log.warn("User not found for ID: {}", userId);
            throw new UserNotFoundException("Could not find user with id " + userId);
        }
        return Optional.of(convertToDto(user));
    }

    @Override
    public void editUserProfile(Integer userId, UserDto userDto) {
        log.info("Editing profile for user ID: {}", userId);
        User existingUser = userDao.findById(userId);
        if (existingUser == null) {
            log.warn("Edit failed: user not found with ID: {}", userId);
            throw new UserNotFoundException("User not found");
        }

        if (!existingUser.getEmail().equals(userDto.getEmail()) && userDao.existsByEmail(userDto.getEmail())) {
            log.warn("Edit failed: new email {} already taken", userDto.getEmail());
            throw new BadRequestException("User with this email already exists");
        }

        if (!("employer".equals(userDto.getAccountType()) || "applicant".equals(userDto.getAccountType()))) {
            log.error("Invalid account type during edit: {}", userDto.getAccountType());
            throw new BadRequestException("User should be either employer or applicant");
        }

        existingUser.setName(userDto.getName());
        existingUser.setAge(userDto.getAge());
        existingUser.setSurname(userDto.getSurname());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setAvatar(userDto.getAvatar());
        existingUser.setAccountType(userDto.getAccountType().toLowerCase());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        int rowsAffected = userDao.updateUser(existingUser);
        if (rowsAffected == 0) {
            log.error("Failed to update user profile for ID: {}", userId);
            throw new DatabaseOperationException("Error updating user profile");
        }

        log.info("User profile updated successfully for ID: {}", userId);
    }

    @Override
    public Optional<List<UserDto>> getApplicantsForVacancy(Integer vacancyId) {
        log.info("Fetching applicants for vacancy ID: {}", vacancyId);
        List<User> applicants = userDao.getApplicantsForVacancy(vacancyId);

        List<UserDto> userDtos = applicants.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return userDtos.isEmpty() ? Optional.empty() : Optional.of(userDtos);
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .accountType(user.getAccountType())
                .build();
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        log.debug("Authenticating user with email: {}", email);

        if (!userDao.existsByEmail(email)) {
            log.warn("User not found for email: {}", email);
            return false;
        }
        User user = userDao.findByEmail(email);

        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatches) {
            log.warn("Incorrect password for email: {}", email);
            return false;
        }

        log.info("User authenticated successfully: {}", email);
        return true;
    }

    @Override
    public void updateUserProfile(String email, UserDto userDto) {
        if (!userDao.existsByEmail(email)) {
            throw new UserNotFoundException("User not found");
        }
        User user = userDao.findByEmail(email);
        userDto.setAccountType(user.getAccountType());
        editUserProfile(user.getId(), userDto);
    }

}
