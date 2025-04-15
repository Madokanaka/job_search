package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.UserEditDto;
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
                vacancies.ifPresent(vacancyDtos -> model.addAttribute("vacancies", vacancyDtos));
                model.addAttribute("view", "vacancies");
            }
            if ("applicant".equals(userDto.get().getAccountType()) || "admin".equals(userDto.get().getAccountType())) {
                Optional<List<ResumeDto>> resumes = resumeService.getResumesByUserId(userId);
                resumes.ifPresent(resumeDtos -> model.addAttribute("resumes", resumeDtos));
                model.addAttribute("view", "resume");
            }
            model.addAttribute("userEdit", userService.fromDtoToUserEditDto(userDto.get()));
            return "profile";
        } else {
            return "error";
        }
    }

    @PutMapping("")
    public String updateUserProfile(@AuthenticationPrincipal User principal, @ModelAttribute("userEdit") @Valid UserEditDto userEditDto,
                                    BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            UserDto user = userService.updateUserProfile(principal.getUsername(), userEditDto);

            model.addAttribute("user", user);
            return "redirect:/profile";

        }

        Integer userId = userService.findUserByEmail(principal.getUsername()).get().getId();

        Optional<UserDto> userDto = userService.getUserById(userId);
        if (userDto.isPresent()) {
            model.addAttribute("user", userDto.get());
            if ("employer".equals(userDto.get().getAccountType()) || "admin".equals(userDto.get().getAccountType())) {
                Optional<List<VacancyDto>> vacancies = vacancyService.getVacanciesByUserId(userId);
                vacancies.ifPresent(vacancyDtos -> model.addAttribute("vacancies", vacancyDtos));
                model.addAttribute("view", "vacancies");
            }
            if ("applicant".equals(userDto.get().getAccountType()) || "admin".equals(userDto.get().getAccountType())) {
                Optional<List<ResumeDto>> resumes = resumeService.getResumesByUserId(userId);
                resumes.ifPresent(resumeDtos -> model.addAttribute("resumes", resumeDtos));
                model.addAttribute("view", "resume");
                model.addAttribute("showEditModal", true);
                model.addAttribute("userEdit", userEditDto);

            }
        }
        return "profile";
    }
}
