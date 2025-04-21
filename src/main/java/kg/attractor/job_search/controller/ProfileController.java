package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.UserEditDto;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.ImageService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final VacancyService vacancyService;
    private final ResumeService resumeService;
    private final ImageService imageService;

    @GetMapping()
    public String getUserProfile(@AuthenticationPrincipal User principal,
                                 @RequestParam(defaultValue = "0") String page,
                                 @RequestParam(defaultValue = "6") String size,
                                 Model model) {
        Integer userId = userService.findUserByEmail(principal.getUsername()).get().getId();

        Optional<UserDto> userDto = userService.getUserById(userId);
        if (userDto.isPresent()) {
            model.addAttribute("user", userDto.get());
            if ("employer".equals(userDto.get().getAccountType()) || "admin".equals(userDto.get().getAccountType())) {
                Page<VacancyDto> vacanciesPage = vacancyService.getVacanciesByUserIdPaged(userId, page, size);
                model.addAttribute("vacancies", vacanciesPage.getContent());
                model.addAttribute("totalPages", vacanciesPage.getTotalPages());
                model.addAttribute("currentPage", vacanciesPage.getNumber());
                model.addAttribute("view", "vacancies");
            }
            if ("applicant".equals(userDto.get().getAccountType()) || "admin".equals(userDto.get().getAccountType())) {
                Optional<List<ResumeDto>> resumes = resumeService.getResumesByUserId(userId);
                resumes.ifPresent(resumeDtos -> model.addAttribute("resumes", resumeDtos));
                model.addAttribute("view", "resume");
            }
            model.addAttribute("userEdit", userService.fromDtoToUserEditDto(userDto.get()));
            return "profiles/profile";
        } else {
            return "error";
        }
    }

    @GetMapping("{profileId}")
    public String getAnotherUserProfile(@AuthenticationPrincipal User principal,
                                        @RequestParam(defaultValue = "0") String page,
                                        @RequestParam(defaultValue = "6") String size,
                                        Model model,
                                        @PathVariable String profileId) {
        Optional<UserDto> userDto = userService.getUserById(profileId);
        model.addAttribute("user", userDto.get());
        if (principal != null && profileId.equals(userService.findUserByEmail(principal.getUsername()).get().getId())) {
            if ("employer".equals(userDto.get().getAccountType()) || "admin".equals(userDto.get().getAccountType())) {
                Page<VacancyDto> vacanciesPage = vacancyService.getVacanciesByUserIdPaged(userDto.get().getId(), page, size);
                model.addAttribute("vacancies", vacanciesPage.getContent());
                model.addAttribute("totalPages", vacanciesPage.getTotalPages());
                model.addAttribute("currentPage", vacanciesPage.getNumber());
                model.addAttribute("view", "vacancies");
            }
            if ("applicant".equals(userDto.get().getAccountType()) || "admin".equals(userDto.get().getAccountType())) {
                Optional<List<ResumeDto>> resumes = resumeService.getResumesByUserId(userDto.get().getId());
                resumes.ifPresent(resumeDtos -> model.addAttribute("resumes", resumeDtos));
                model.addAttribute("view", "resume");
            }
            model.addAttribute("userEdit", userService.fromDtoToUserEditDto(userDto.get()));
            return "profiles/profile";
        }

        return "profiles/another_profile";
    }

    @PutMapping("")
    public String updateUserProfile(@AuthenticationPrincipal User principal, @ModelAttribute @Valid UserEditDto userEditDto,
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

            }
        }
        userEditDto.setAge(userDto.get().getAge());
        model.addAttribute("userEdit", userEditDto);
        return "profiles/profile";
    }

    @PostMapping("/avatar")
    public String updateAvatar(@AuthenticationPrincipal User principal,
                               @RequestParam("file") MultipartFile file,
                               Model model) {

        if (!file.isEmpty()) {
            String fileName = principal.getUsername() + "_avatar_" + file.getOriginalFilename();

            imageService.uploadImage(principal, file);
        }

        Integer userId = userService.findUserByEmail(principal.getUsername()).get().getId();

        Optional<UserDto> userDto = userService.getUserById(userId);
        model.addAttribute("user", userDto.get());
        return "redirect:/profile";
    }
}
