package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/{vacancyId}/respond")
    public ResponseEntity<?> respondToVacancy(
            @PathVariable Integer vacancyId,
            @RequestParam Integer resumeId) {
        RespondenApplicantDto response = applicationService.respondToVacancy(resumeId, vacancyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
