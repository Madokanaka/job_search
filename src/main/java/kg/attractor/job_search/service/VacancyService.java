package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.model.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VacancyService {

    void createVacancy(VacancyDto vacancyDto, Integer userId);

    void editVacancy(Integer vacancyId, VacancyDto vacancyDto);

    void deleteVacancy(Integer vacancyId);

    List<VacancyDto> getAllVacancies();

    Optional<List<VacancyDto>> getVacanciesByUserId(Integer userId);


    Vacancy getVacancyModelById(Integer vacancyId);

    Page<VacancyDto> getAllVacanciesPaged(String pageNumber, String pageSize, String str);

    Page<VacancyDto> getVacanciesByUserIdPaged(Integer userId, String pageNumber, String pageSize);

    Optional<List<VacancyDto>> getVacanciesByCategory(Integer categoryId);

    Optional<List<VacancyDto>> getVacanciesUserRespondedTo(Integer userId);

    Optional<VacancyDto> getVacancyById(Integer vacancyId);

}
