package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.model.Vacancy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    @PostMapping
    public ResponseEntity<?> createVacancy(@RequestBody VacancyDto vacancyDto) {
        //TODO Логика создания вакансии
        return ResponseEntity.status(HttpStatus.CREATED).body("Vacancy created successfully");
    }

    @PutMapping("/{vacancyId}")
    public ResponseEntity<?> editVacancy(@PathVariable Integer vacancyId, @RequestBody VacancyDto vacancyDto) {
        //TODO Логика редактирования вакансии
        return ResponseEntity.ok().body("Vacancy updated");
    }

    @DeleteMapping("/{vacancyId}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Integer vacancyId) {
        //TODO Логика удаления вакансии
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<Vacancy>> getAllVacancies() {
        //TODO Логика получения всех активных вакансий
        return ResponseEntity.ok(List.of(new Vacancy()));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Vacancy>> getVacanciesByCategory(@PathVariable Integer categoryId) {
        //TODO Логика получения вакансий по категории
        return ResponseEntity.ok(List.of(new Vacancy()));
    }

    @GetMapping("/{vacancyId}")
    public ResponseEntity<Vacancy> getVacancy(@PathVariable Integer vacancyId) {
        //TODO Логика получения вакансии по id
        return ResponseEntity.ok(new Vacancy());
    }
}
