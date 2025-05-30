package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplicationService applicationService;

    @GetMapping
    public String getApplications(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<RespondenApplicantDto> applications = applicationService.getApplicationsByUser(principal, pageable);

        model.addAttribute("applications", applications.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", applications.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("userRole", applicationService.getUserRole(principal));

        return "applications";
    }
}