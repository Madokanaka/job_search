package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;
    private final UserService userService;
    private final ResumeService resumeService;

    @GetMapping
    public String getAllVacancies(@AuthenticationPrincipal User principal,
                                  Model model,
                                  @RequestParam(defaultValue = "0") String page,
                                  @RequestParam(defaultValue = "6") String size) {

        Page<VacancyDto> vacancyPage = vacancyService.getAllVacanciesPaged(page, size);

        if (principal != null) {
            userService.findUserByEmail(principal.getUsername())
                    .ifPresent(user -> model.addAttribute("accountType", user.getAccountType()));
        }

        model.addAttribute("vacancies", vacancyPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vacancyPage.getTotalPages());

        return "vacancies/vacancies";
    }


    @GetMapping("/create")
    public String createVacancy(Model model) {
        Map<Integer, String> categories = resumeService.getCategories();
        VacancyDto vacancyDto = VacancyDto.builder()
                .categoryId(1)
                .build();
        model.addAttribute("categories", categories);
        model.addAttribute("vacancyDto", vacancyDto);
        return "vacancies/create";
    }

    @PostMapping("/create")
    public String createVacancy(@Valid @ModelAttribute VacancyDto vacancyDto, BindingResult bindingResult,
                                @AuthenticationPrincipal User principal, Model model) {
        if (!bindingResult.hasErrors()) {
            vacancyService.createVacancy(vacancyDto, userService.findUserByEmail(principal.getUsername()).get().getId());
            return "redirect:/profile";
        }
        model.addAttribute("categories", resumeService.getCategories());

        model.addAttribute("vacancyDto", vacancyDto);
        return "vacancies/create";
    }

    @GetMapping("{vacancyId}/edit")
    public String getEditVacancyPage(@PathVariable Integer vacancyId, Model model) {
        Map<Integer, String> categories = resumeService.getCategories();
        VacancyDto vacancyDto = vacancyService.getVacancyById(vacancyId).get();
        vacancyDto.setCategoryId(1);
        model.addAttribute("categories", categories);
        model.addAttribute("vacancyDto", vacancyDto);
        return "vacancies/edit";
    }

    @PutMapping("{vacancyId}/edit")
    public String editVacancy(@Valid @ModelAttribute VacancyDto vacancyDto, BindingResult bindingResult,
                              @PathVariable Integer vacancyId, @AuthenticationPrincipal User principal, Model model) {
        vacancyDto.setId(vacancyId);
        if (!bindingResult.hasErrors()) {
            vacancyService.editVacancy(vacancyId, vacancyDto);
            model.addAttribute("user", userService.findUserByEmail(principal.getUsername()).get());
            return "redirect:/profile";
        }
        model.addAttribute("categories", resumeService.getCategories());

        model.addAttribute("vacancyDto", vacancyDto);
        return "vacancies/edit";
    }

}
