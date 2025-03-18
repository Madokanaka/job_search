package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.VacancyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VacancyServiceImpl implements VacancyService {

    @Override
    public VacancyDto createVacancy(VacancyDto vacancyDto, Integer employerId) {
        // TODO: Создать новую вакансию на основе данных из vacancyDto
        // TODO: Связать вакансию с работодателем через employerId
        // TODO: Сохранить вакансию в хранилище

        return new VacancyDto();
    }

    @Override
    public VacancyDto editVacancy(Integer vacancyId, VacancyDto vacancyDto) {
        // TODO: Найти вакансию по ID
        // TODO: Обновить её на основе данных из vacancyDto
        // TODO: Сохранить обновлённую вакансию

        return new VacancyDto();
    }

    @Override
    public void deleteVacancy(Integer vacancyId) {
        // TODO: Найти вакансию по ID
        // TODO: Удалить вакансию из хранилища
    }

    @Override
    public List<Vacancy> getAllVacancies() {
        // TODO: Получить список всех активных вакансий
        return List.of(new Vacancy());
    }

    @Override
    public List<VacancyDto> getVacanciesByCategory(Integer categoryId) {
        // TODO: Найти вакансии по категории
        return List.of(new VacancyDto());
    }

    @Override
    public Optional<VacancyDto> getVacancyById(Integer vacancyId) {
        // TODO: Найти вакансию по ID
        return Optional.empty();
    }
}
