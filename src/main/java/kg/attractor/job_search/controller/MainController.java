package kg.attractor.job_search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MainController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("world", "world");
        return "index";
    }
}
