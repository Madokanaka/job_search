package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.VacancyDto;

import java.util.List;
import java.util.Optional;

public interface VacancyService {

    VacancyDto createVacancy(VacancyDto vacancyDto);

    VacancyDto editVacancy(Integer vacancyId, VacancyDto vacancyDto);

    void deleteVacancy(Integer vacancyId);

    List<VacancyDto> getAllVacancies();

    Optional<VacancyDto> getVacancyById(Integer vacancyId);

    Optional<List<VacancyDto>> getVacanciesByCategory(Integer categoryId);

    Optional<List<VacancyDto>> getVacanciesUserRespondedTo(Integer userId);
}
