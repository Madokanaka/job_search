package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.model.User;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.util.FileUtil;
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
    public String uploadAvatar(MultipartFile file) {
        // TODO логика привязки аватара к пользователю
        FileUtil fu = new FileUtil();
        String fileName = fu.saveUploadFile(file, "images/");
        return fileName;
    }
}
