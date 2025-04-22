package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;
    @GetMapping
    public String getAllResumes(@AuthenticationPrincipal User principal,
            @RequestParam(defaultValue = "0") String page,
                                @RequestParam(defaultValue = "6") String size,
                                Model model) {

        Page<ResumeDto> resumePage = resumeService.getAllResumesPaged(page, size);
        model.addAttribute("user", userService.findUserByEmail(principal.getUsername()).get());
        model.addAttribute("resumes", resumePage.getContent());
        model.addAttribute("currentPage", resumePage.getNumber()    );
        model.addAttribute("totalPages", resumePage.getTotalPages());

        return "resumes/resumes";
    }


    @GetMapping("/create")
    public String createResume(@AuthenticationPrincipal User principal, Model model) {
        ResumeDto resumeDto = ResumeDto.builder()
                .categoryId(1)
                .workExperienceInfoList(List.of(new WorkExperienceInfoDto()))
                .educationInfoList(List.of(new EducationInfoDto()))
                .build();

        resumeDto.setCategoryId(1);
        model.addAttribute("categories", resumeService.getCategories());
        model.addAttribute("user", userService.findUserByEmail(principal.getUsername()).get());
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
        model.addAttribute("user", userService.findUserByEmail(principal.getUsername()).get());
        model.addAttribute("categories", resumeService.getCategories());
        model.addAttribute("resumeDto", resumeDto);
        return "resumes/create";
    }

    @GetMapping("{resumeId}/edit")
    public String getEditPage(@AuthenticationPrincipal User principal, @PathVariable Integer resumeId, Model model) {
        ResumeDto resumeDto = resumeService.getResumeById(resumeId);

        resumeDto.setCategoryId(1);
        model.addAttribute("user", userService.findUserByEmail(principal.getUsername()).get());
        model.addAttribute("categories", resumeService.getCategories());
        model.addAttribute("resumeDto", resumeDto);

        return "resumes/edit";
    }

    @PutMapping("{resumeId}/edit")
    public String editResume(@Valid @ModelAttribute ResumeDto resumeDto, BindingResult bindingResult,
                               @PathVariable Integer resumeId,
                               @AuthenticationPrincipal User principal, Model model) {
        resumeDto.setId(resumeId);
        if (!bindingResult.hasErrors()) {
            resumeService.editResume(resumeId, resumeDto);

            return "redirect:/profile";
        }
        model.addAttribute("user", userService.findUserByEmail(principal.getUsername()).get());
        model.addAttribute("categories", resumeService.getCategories());

        model.addAttribute("resumeDto", resumeDto);
        return "resumes/edit";
    }
}
