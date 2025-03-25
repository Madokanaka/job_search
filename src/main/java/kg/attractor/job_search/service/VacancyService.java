package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.VacancyDto;

import java.util.List;
import java.util.Optional;

public interface VacancyService {

    void createVacancy(VacancyDto vacancyDto, Integer userId);

    void editVacancy(Integer vacancyId, VacancyDto vacancyDto);

    void deleteVacancy(Integer vacancyId);

    List<VacancyDto> getAllVacancies();

    Optional<List<VacancyDto>> getVacanciesByCategory(Integer categoryId);

    Optional<List<VacancyDto>> getVacanciesUserRespondedTo(Integer userId);

    Optional<List<UserDto>> getApplicantsForVacancy(Integer vacancyId);

    Optional<VacancyDto> getVacancyById(Integer vacancyId);
}
