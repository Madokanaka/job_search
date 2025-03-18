package kg.attractor.job_search.controller;

import kg.attractor.job_search.model.RespondedApplicant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @PostMapping("/{vacancyId}/respond")
    public ResponseEntity<?> respondToVacancy(@PathVariable Integer vacancyId, @RequestParam Integer resumeId) {
        //TODO Логика отклика на вакансию
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully responded to vacancy");
    }

    @GetMapping("/{vacancyId}/responses")
    public ResponseEntity<String> getRespondedApplicants(@PathVariable Integer vacancyId) {
        //TODO Логика получения откликнувшихся соискателей
        return ResponseEntity.ok("List of responded applicants");
    }
}
