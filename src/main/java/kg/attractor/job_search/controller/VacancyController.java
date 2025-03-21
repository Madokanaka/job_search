package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    @PostMapping
    public ResponseEntity<VacancyDto> createVacancy(@RequestBody VacancyDto vacancyDto) {
        VacancyDto createdVacancy = vacancyService.createVacancy(vacancyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVacancy);
    }

    @PutMapping("/{vacancyId}")
    public ResponseEntity<VacancyDto> editVacancy(@PathVariable Integer vacancyId, @RequestBody VacancyDto vacancyDto) {
        VacancyDto updatedVacancy = vacancyService.editVacancy(vacancyId, vacancyDto);
        return ResponseEntity.ok(updatedVacancy);
    }

    @DeleteMapping("/{vacancyId}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Integer vacancyId) {
        vacancyService.deleteVacancy(vacancyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<VacancyDto>> getAllVacancies() {
        List<VacancyDto> vacancies = vacancyService.getAllVacancies();
        return ResponseEntity.ok(vacancies);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<VacancyDto>> getVacanciesByCategory(@PathVariable Integer categoryId) {
        Optional<List<VacancyDto>> vacancies = vacancyService.getVacanciesByCategory(categoryId);
        return vacancies.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{vacancyId}")
    public ResponseEntity<VacancyDto> getVacancy(@PathVariable Integer vacancyId) {
        Optional<VacancyDto> vacancy = vacancyService.getVacancyById(vacancyId);
        return vacancy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
