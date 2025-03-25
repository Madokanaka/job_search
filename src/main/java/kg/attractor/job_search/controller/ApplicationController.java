package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/{vacancyId}/respond")
    public ResponseEntity<?> respondToVacancy(
            @PathVariable Integer vacancyId,
            @RequestParam Integer resumeId) {
        try {
            RespondenApplicantDto response = applicationService.respondToVacancy(resumeId, vacancyId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
