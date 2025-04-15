package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;
    @GetMapping
    public String getAllResumes(Model model) {
        List<ResumeDto> resumes = resumeService.getAllResumes();
        model.addAttribute("resumes", resumes);
        return "resumes/resumes";
    }

    @GetMapping("/create")
    public String createResume(Model model) {
        Map<Integer, String> categories = resumeService.getCategories();
        ResumeDto resumeDto = ResumeDto.builder()
                .categoryId(1)
                .workExperienceInfoList(List.of(new WorkExperienceInfoDto()))
                .educationInfoList(List.of(new EducationInfoDto()))
                .build();
        System.out.println(resumeDto.getEducationInfoList().get(0));

        resumeDto.setCategoryId(1);
        model.addAttribute("categories", resumeService.getCategories());
        model.addAttribute("resumeDto", resumeDto);
        return "resumes/create";
    }

    @PostMapping("/create")
    public String createResume(@Valid @ModelAttribute ResumeDto resumeDto, BindingResult bindingResult,
                               @AuthenticationPrincipal User principal, Model model) {
        if (!bindingResult.hasErrors()) {
            resumeService.createResume(resumeDto, userService.findUserByEmail(principal.getUsername()).get().getId());
            model.addAttribute("user", userService.findUserByEmail(principal.getUsername()).get());
            model.addAttribute("categories", resumeService.getCategories());
            return "redirect:/profile";
        }
        model.addAttribute("resumeDto", resumeDto);
        return "resumes/create";
    }

    @GetMapping("{resumeId}/edit")
    public String getEditPage(@PathVariable Integer resumeId, Model model) {
        Map<Integer, String> categories = resumeService.getCategories();
        ResumeDto resumeDto = resumeService.getResumeById(resumeId);

        resumeDto.setCategoryId(1);
        model.addAttribute("categories", resumeService.getCategories());
        model.addAttribute("resumeDto", resumeDto);

        return "resumes/edit";
    }

    @PutMapping("{resumeId}/edit")
    public String editResume(@Valid @ModelAttribute ResumeDto resumeDto, BindingResult bindingResult,
                               @PathVariable Integer resumeId,
                               @AuthenticationPrincipal User principal, Model model) {
        if (!bindingResult.hasErrors()) {
            resumeService.editResume(resumeId, resumeDto);
            model.addAttribute("user", userService.findUserByEmail(principal.getUsername()).get());
            model.addAttribute("categories", resumeService.getCategories());

            return "redirect:/profile";
        }
        model.addAttribute("resumeDto", resumeDto);
        return "resumes/edit";
    }
}
