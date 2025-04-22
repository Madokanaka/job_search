package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/companies")
    public String getCompaniesPage(@AuthenticationPrincipal User principal,
                            Model model,
                            @RequestParam(defaultValue = "0") String page,
                            @RequestParam(defaultValue = "6") String size) {
    Page<UserDto> employers = userService.getEmployers(page, size);
        if (principal != null) {
            userService.findUserByEmail(principal.getUsername())
                    .ifPresent(user -> {
                        model.addAttribute("accountType", user.getAccountType());
                        model.addAttribute("user", user);
                    });

        }
        model.addAttribute("employers", employers.getContent());
        model.addAttribute("currentPage", employers.getNumber());
        model.addAttribute("totalPages", employers.getTotalPages());

    return "users/employers";
    }

}
