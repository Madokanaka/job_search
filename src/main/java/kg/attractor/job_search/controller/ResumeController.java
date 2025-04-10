package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    @GetMapping
    public String getAllResumes(Model model) {
        List<ResumeDto> resumes = resumeService.getAllResumes();
        model.addAttribute("resumes", resumes);
        return "resumes/resumes";
    }
}
