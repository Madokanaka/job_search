package kg.attractor.job_search.controller;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.UserDto;
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

    @PostMapping("/create")
    public ResponseEntity<?> createVacancy(@Valid @RequestBody VacancyDto vacancyDto, @RequestParam Integer userId) {
        vacancyService.createVacancy(vacancyDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vacancy was created");
    }

    @PutMapping("/{vacancyId}/edit")
    public ResponseEntity<?> editVacancy(@PathVariable Integer vacancyId, @Valid @RequestBody VacancyDto vacancyDto) {
        vacancyService.editVacancy(vacancyId, vacancyDto);
        return ResponseEntity.ok("Vacancy was edited");
    }

    @DeleteMapping("/{vacancyId}/delete")
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

    @GetMapping("/user/{userId}/responded")
    public ResponseEntity<List<VacancyDto>> getVacanciesUserRespondedTo(@PathVariable Integer userId) {
        Optional<List<VacancyDto>> vacancies = vacancyService.getVacanciesUserRespondedTo(userId);
        return vacancies.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
