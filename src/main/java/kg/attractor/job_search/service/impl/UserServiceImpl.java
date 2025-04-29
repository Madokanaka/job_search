package kg.attractor.job_search.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.UserEditDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.DatabaseOperationException;
import kg.attractor.job_search.exception.RecordAlreadyExistsException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.repository.RolesRepository;
import kg.attractor.job_search.repository.UserRepository;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.util.CommonUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public void registerUser(UserDto userDto) {
        log.info("Registering user: {}", userDto.getEmail());

        if (userRepository.existsByEmail(userDto.getEmail().strip())) {
            throw new RecordAlreadyExistsException("User with this email already exists");
        }

        validateAccountType(userDto.getAccountType());

        if (!rolesRepository.existsByRoleName(userDto.getAccountType().toUpperCase())) {
            throw new BadRequestException("Invalid account type");
        }

//        Role userRole = rolesRepository.findByRoleName(userDto.getAccountType().toUpperCase());

        User user = User.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .avatar(userDto.getAvatar())
                .accountType(userDto.getAccountType().toLowerCase())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .enabled(true)
                .build();

        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error saving user", e);
            throw new DatabaseOperationException("Error creating user");
        }

        log.info("User registered: {}", user.getEmail());
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto)
                .or(() -> {
                    throw new UserNotFoundException("User with this email does not exist");
                });
    }

    @Override
    public User findUserModelByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new UserNotFoundException("User with this email does not exist");
                });
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getApplicantById(Integer userId) {
        return userRepository.findById(userId)
                .filter(u -> "APPLICANT".equalsIgnoreCase(u.getAccountType()))
                .map(this::convertToDto)
                .or(() -> {
                    throw new UserNotFoundException("Could not find applicant with id " + userId);
                });
    }

    @Override
    public Optional<UserDto> getEmployeeById(Integer userId) {
        return userRepository.findById(userId)
                .filter(u -> "EMPLOYER".equalsIgnoreCase(u.getAccountType()))
                .map(this::convertToDto)
                .or(() -> {
                    throw new UserNotFoundException("Could not find employee with id " + userId);
                });
    }

    @Override
    public Optional<UserDto> getUserById(Integer userId) {
        return userRepository.findById(userId)
                .map(this::convertToDto)
                .or(() -> {
                    throw new UserNotFoundException("Could not find user with id " + userId);
                });
    }

    @Override
    public User getUserModelById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new UserNotFoundException("Could not find user with id " + userId);
                });
    }

    @Override
    public Optional<UserDto> getUserById(String userIdInString) {
        int userId;
        try {
            userId = Integer.parseInt(userIdInString);
        } catch (NumberFormatException e) {
            throw new BadRequestException("User ID must be a valid number");
        }
        return userRepository.findById(userId)
                .map(this::convertToDto)
                .or(() -> {
                    throw new UserNotFoundException("Could not find user with id " + userId);
                });
    }

    @Override
    public void editUserProfile(Integer userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }

        validateAccountType(userDto.getAccountType());

        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAvatar(userDto.getAvatar());
        user.setAccountType(userDto.getAccountType().toLowerCase());

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new DatabaseOperationException("Error updating user profile");
        }
    }

    @Override
    public Optional<List<UserDto>> getApplicantsForVacancy(Integer vacancyId) {
        List<User> applicants = userRepository.findApplicantsByVacancyId(vacancyId);
        List<UserDto> dtos = applicants.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return dtos.isEmpty() ? Optional.empty() : Optional.of(dtos);
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException("User with this email does not exist");
        }
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public UserDto updateUserProfile(String email, UserEditDto userEditDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setName(userEditDto.getName());
        user.setSurname(userEditDto.getSurname());
        user.setAge(userEditDto.getAge());
        user.setPhoneNumber(userEditDto.getPhoneNumber());

        userRepository.save(user);

        return convertToDto(user);
    }

    @Override
    public UserEditDto fromDtoToUserEditDto(UserDto userDto) {
        return UserEditDto.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .age(userDto.getAge())
                .phoneNumber(userDto.getPhoneNumber())
                .build();
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

    private void validateAccountType(String type) {
        if (!("employer".equalsIgnoreCase(type) || "applicant".equalsIgnoreCase(type))) {
            throw new BadRequestException("User role should be either employer or applicant");
        }
    }

    @Override
    public Page<UserDto> getEmployers(String pageNumber, String pageSize) {
        int page = parsePageParameter(pageNumber);
        int size = parseSizeParameter(pageSize, 6);
        Pageable pageable = PageRequest.of(page, size);
        Page<User> employers = userRepository.findByAccountType("employer", pageable);

        if (employers.getTotalPages() > 0 && page >= employers.getTotalPages()) {
            log.warn("Запрашиваемая страница больше максимальной, выбираем последнюю страницу");
            pageable = PageRequest.of(employers.getTotalPages() - 1, size);
            employers = userRepository.findByAccountType("employer", pageable);
        }

        return employers.map(this::convertToDto);
    }

    private int parsePageParameter(String page) {
        try {
            int pageNumber = Integer.parseInt(page);
            if (pageNumber < 0) {
                log.warn("Page index less than 0, setting to 0");
                return 0;
            }
            return pageNumber;
        } catch (NumberFormatException e) {
            log.warn("Invalid page parameter: {}. Setting to default 0", page);
            return 0;
        }
    }

    private int parseSizeParameter(String size, int defaultValue) {
        try {
            int pageSize = Integer.parseInt(size);
            if (pageSize <= 0 || pageSize > 100) {
                log.warn("Invalid size parameter: {}. Setting to default 6", size);
                return defaultValue;
            }
            return pageSize;
        } catch (NumberFormatException e) {
            log.warn("Invalid size parameter: {}. Setting to default 6", size);
            return defaultValue;
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }


    @Override
    public void saveAvatar(Long userId, String fileName) {
        userRepository.saveAvatar(userId, fileName);

        log.debug("Image saved with fileName={}", fileName);
    }


    private void updateResetPasswordToken(String token, String email) {
        User user = userRepository.findByEmail(email.strip()).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setResetPasswordToken(token);
        userRepository.saveAndFlush(user);
    }


    @Override
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token).orElseThrow(() -> new UserNotFoundException("User not found"));
    }


    @Override
    public void updatePassword(User user, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void makeResetPasswordLnk(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        String token = UUID.randomUUID().toString();
        updateResetPasswordToken(token, email);
        String resetPasswordLnk = CommonUtilities.getSiteUrl(request) + "/auth/reset_password?token=" + token;
        emailService.sendEmail(email, resetPasswordLnk);
    }
}
