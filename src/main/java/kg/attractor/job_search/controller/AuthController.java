package kg.attractor.job_search.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/registration";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "auth/login";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserDto userDto,
                               BindingResult bindingResult,
                               Model model) {
        if (userService.existsByEmail(userDto.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "User with this email already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "auth/registration";
        }

        userService.registerUser(userDto);

        return "redirect:/auth/login";
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordPage(Model model) {
        return "auth/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        try {
            userService.makeResetPasswordLnk(request);
            model.addAttribute("message", "Password reset link has been sent");
        } catch (UserNotFoundException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        } catch (MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }
        return "auth/forgot_password_form";
    }

    @GetMapping("reset_password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        try {
            userService.getByResetPasswordToken(token);
            model.addAttribute("token", token);
        } catch (UserNotFoundException e) {
            model.addAttribute("error", "Invalid token");
        }
        return "auth/reset_password_form";
    }

    @PostMapping("reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        try {
            User user = userService.getByResetPasswordToken(token);
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password");
        } catch (UserNotFoundException e) {
            model.addAttribute("error", "Invalid token");
        }
        return "/auth/message";
    }

}

