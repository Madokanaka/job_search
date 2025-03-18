package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.service.ResumeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Override
    public Resume createResume(ResumeDto resumeDto, Integer userId) {
        // TODO: Создать новое резюме на основе данных из resumeDto
        // TODO: Связать резюме с пользователем через userId
        // TODO: Сохранить резюме в хранилище

        return new Resume();
    }

    @Override
    public ResumeDto editResume(Integer resumeId, ResumeDto resumeDto) {
        // TODO: Найти резюме по ID
        // TODO: Обновить его на основе resumeDto
        // TODO: Сохранить обновлённое резюме

        return new ResumeDto();
    }

    @Override
    public void deleteResume(Integer resumeId) {
        // TODO: Найти резюме по ID
        // TODO: Удалить резюме из хранилища
    }

    @Override
    public List<ResumeDto> getAllResumes() {
        // TODO: Получить список всех резюме
        return List.of(new ResumeDto());
    }

    @Override
    public Optional<ResumeDto> getResumeById(Integer resumeId) {
        // TODO: Найти резюме по ID
        return Optional.empty();
    }
}
