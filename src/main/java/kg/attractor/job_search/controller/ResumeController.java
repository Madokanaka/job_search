package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.service.ContactInfoService;
import kg.attractor.job_search.service.EducationInfoService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/create")
    public ResponseEntity<?> createResume(@Valid @RequestBody ResumeDto resumeDto, @RequestParam Integer userId) {
        resumeService.createResume(resumeDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body("Resume created");

    }

    @PutMapping("/{resumeId}/edit")
    public ResponseEntity<?> editResume(@PathVariable Integer resumeId, @Valid @RequestBody ResumeDto resumeDto) {
        resumeService.editResume(resumeId, resumeDto);
        return ResponseEntity.status(HttpStatus.OK).body("Resume was edited");
    }

    @DeleteMapping("/{resumeId}/delete")
    public ResponseEntity<?> deleteResume(@PathVariable Integer resumeId) {
        resumeService.deleteResume(resumeId);

        return ResponseEntity.status(HttpStatus.OK).body("Resume was deleted");
    }

    @GetMapping
    public ResponseEntity<List<ResumeDto>> getAllResumes() {
        List<ResumeDto> resumes = resumeService.getAllResumes();
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getResumesByUserId(@PathVariable Integer userId) {
        Optional<List<ResumeDto>> resumes = resumeService.getResumesByUserId(userId);

        if (resumes.isPresent()) {
            return ResponseEntity.ok(resumes.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resumes not found for user");
    }
}
