package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.model.Resume;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    @PostMapping
    public ResponseEntity<?> createResume(@RequestBody ResumeDto resumeDto) {
        // TODO Логика создания резюме
        return ResponseEntity.status(HttpStatus.CREATED).body("Resume created");
    }

    @PutMapping("/{resumeId}")
    public ResponseEntity<?> editResume(@PathVariable Integer resumeId, @RequestBody ResumeDto resumeDto) {
        //TODO Логика редактирования резюме
        return ResponseEntity.ok().body("Resume edited");
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable Integer resumeId) {
        //TODO Логика удаления резюме
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<ResumeDto>> getAllResumes() {
        //TODO Логика получения всех резюме
        return ResponseEntity.ok(List.of(new ResumeDto()));
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<?> getResume(@PathVariable Integer resumeId) {
        //TODO Логика получения резюме по id
        return ResponseEntity.ok().body("Resume found");
    }
}
