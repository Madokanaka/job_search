package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final VacancyService vacancyService;
    private final ResumeService resumeService;

    @GetMapping("")
    public String getUserProfile(@AuthenticationPrincipal User principal, Model model) {
        Integer userId = userService.findUserByEmail(principal.getUsername()).get().getId();

        Optional<UserDto> userDto = userService.getUserById(userId);
        if (userDto.isPresent()) {
            model.addAttribute("user", userDto.get());
            if ("employer".equals(userDto.get().getAccountType()) || "admin".equals(userDto.get().getAccountType())) {
                Optional<List<VacancyDto>> vacancies = vacancyService.getVacanciesByUserId(userId);
                if (vacancies.isPresent()) {
                    model.addAttribute("vacancies", vacancies.get());
                }
                model.addAttribute("view", "vacancies");
            } else if ("applicant".equals(userDto.get().getAccountType()) || "admin".equals(userDto.get().getAccountType())) {
                Optional<List<ResumeDto>> resumes = resumeService.getResumesByUserId(userId);
                if (resumes.isPresent()) {
                    model.addAttribute("resumes", resumes.get());
                }
                model.addAttribute("view", "resume");
            }
            return "profile";
        } else {
            return "error";
        }
    }

    @GetMapping("/edit")
    public String editUserProfile(@AuthenticationPrincipal User principal, Model model) {
        Integer userId = userService.findUserByEmail(principal.getUsername()).get().getId();
        System.out.println(userId);
        Optional<UserDto> userDto = userService.getUserById(userId);
        if (userDto.isPresent()) {
            System.out.println(userDto.get());
            model.addAttribute("user", userDto.get());
            return "editProfile";
        } else {
            return "error";
        }
    }

    @PutMapping("/edit")
    public String updateUserProfile(@AuthenticationPrincipal User principal, @ModelAttribute ("user") @Valid UserDto userDto,
                                    BindingResult bindingResult, Model model) {
        System.out.println(userDto);
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                model.addAttribute("error_" + error.getField(), error.getDefaultMessage());
            }
            return "editProfile";
        }

        Integer userId = userService.findUserByEmail(principal.getUsername()).get().getId();
        userDto.setId(userId);
        userDto.setAccountType(userService.getUserById(userId).get().getAccountType());
        userService.editUserProfile(userId, userDto);

        model.addAttribute("user", userDto);
        return "redirect:/profile";
    }


}
