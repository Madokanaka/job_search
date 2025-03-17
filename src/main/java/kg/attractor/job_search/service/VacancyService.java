package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.model.Vacancy;

import java.util.List;
import java.util.Optional;

public interface VacancyService {
    Vacancy createVacancy(VacancyDto vacancyDto, Integer employerId);

    VacancyDto editVacancy(Integer vacancyId, VacancyDto vacancyDto);

    void deleteVacancy(Integer vacancyId);

    List<Vacancy> getAllVacancies();

    List<VacancyDto> getVacanciesByCategory(Integer categoryId);

    Optional<VacancyDto> getVacancyById(Integer vacancyId);
}
