package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.model.User;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto registerUser(UserDto userDto) {
        // TODO: Проверить, существует ли уже пользователь с таким email
        // TODO: Сохранить данные пользователя в хранилище (например, в JSON)
        return new UserDto();
    }

    @Override
    public Optional<User> login(String email, String password) {
        // TODO: Проверить наличие пользователя по email
        // TODO: Сравнить пароли, если пароли совпадают - возвращаем пользователя
        return Optional.empty();
    }

    @Override
    public String uploadAvatar(Integer userId, MultipartFile file) {
        // TODO: Сохранить изображение в файловую систему или базу данных
        // TODO: Сохранить путь к аватару в данные пользователя

        return "Avatar uploaded successfully";
    }
}
