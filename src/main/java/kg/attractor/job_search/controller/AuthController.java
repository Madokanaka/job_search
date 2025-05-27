package kg.attractor.job_search.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ResetPasswordDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

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
            bindingResult.rejectValue("email", "email.exists", messageSource.getMessage("error.email.exists", null, LocaleContextHolder.getLocale()));
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
            model.addAttribute("message", messageSource.getMessage("message.password.reset.link.sent", null, LocaleContextHolder.getLocale()));
        } catch (UserNotFoundException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        } catch (MessagingException e) {
            model.addAttribute("error", messageSource.getMessage("error.email.sending", null, LocaleContextHolder.getLocale()));
        }
        return "auth/forgot_password_form";
    }

    @GetMapping("reset_password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        try {
            model.addAttribute("resetPasswordDto", new ResetPasswordDto());
            model.addAttribute("token", token);
        } catch (UserNotFoundException e) {
            model.addAttribute("error", messageSource.getMessage("error.invalid.token", null, LocaleContextHolder.getLocale()));
        }
        return "auth/reset_password_form";
    }

    @PostMapping("reset_password")
    public String processResetPassword(@Valid @ModelAttribute("resetPasswordDto") ResetPasswordDto resetPasswordDto,
                                       BindingResult bindingResult,
                                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("token", resetPasswordDto.getToken());
            return "auth/reset_password_form";
        }

        try {
            User user = userService.getByResetPasswordToken(resetPasswordDto.getToken());
            userService.updatePassword(user, resetPasswordDto.getPassword());
            model.addAttribute("message", messageSource.getMessage("message.password.changed", null, LocaleContextHolder.getLocale()));
        } catch (UserNotFoundException e) {
            model.addAttribute("error", messageSource.getMessage("error.invalid.token", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            model.addAttribute("error", messageSource.getMessage("error.email.sending", null, LocaleContextHolder.getLocale()));
        }

        return "/auth/message";
    }
}
