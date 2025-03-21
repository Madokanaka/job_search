package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.UserDao;
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
        user.setAccountType(userDto.getAccountType());
        user.setPassword(userDto.getPassword());

        int rowsAffected = userDao.createUser(user);
        if (rowsAffected == 0) {
            throw new RuntimeException("Error creating user");
        }
    }

    @Override
    public String uploadAvatar(MultipartFile file) {

        try {
            FileUtil fileUtil = new FileUtil();
            return fileUtil.saveUploadFile(file, "images/");
        } catch (Exception e) {
            throw new RuntimeException("Error uploading avatar: " + e.getMessage());
        }
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        return Optional.ofNullable(userDao.findByEmail(email))
                .map(user -> UserDto.builder()
                        .id(user.getId())
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
                        .id(user.getId())
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

}
