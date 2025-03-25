package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = userService.uploadAvatar(file);
            return ResponseEntity.ok("Avatar uploaded successfully: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading avatar: " + e.getMessage());
        }
    }

    @GetMapping("/find-by-email")
    public ResponseEntity<UserDto> findUserByEmail(@RequestParam("email") String email) {
        Optional<UserDto> userDto = userService.findUserByEmail(email);
        return userDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/exists-by-email")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam("email") String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{userId}/applicant")
    public ResponseEntity<UserDto> getApplicantById(@PathVariable Integer userId) {
        Optional<UserDto> userDto = userService.getApplicantById(userId);
        return userDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/user/{userId}/employee")
    public ResponseEntity<UserDto> getEmployeeById(@PathVariable Integer userId) {
        Optional<UserDto> userDto = userService.getEmployeeById(userId);
        return userDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

}
