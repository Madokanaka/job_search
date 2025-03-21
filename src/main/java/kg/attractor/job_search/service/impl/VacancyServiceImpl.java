package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyDao vacancyDao;


    @Override
    public VacancyDto createVacancy(VacancyDto vacancyDto) {
        vacancyDao.createVacancy(vacancyDto);
        return vacancyDto;
    }

    @Override
    public VacancyDto editVacancy(Integer vacancyId, VacancyDto vacancyDto) {
        vacancyDao.updateVacancy(vacancyId, vacancyDto);
        return vacancyDto;
    }

    @Override
    public void deleteVacancy(Integer vacancyId) {
        vacancyDao.deleteVacancy(vacancyId);
    }

    @Override
    public List<VacancyDto> getAllVacancies() {
        return vacancyDao.getAllVacancies();
    }

    @Override
    public Optional<VacancyDto> getVacancyById(Integer vacancyId) {
        return vacancyDao.getVacancyById(vacancyId);
    }

    @Override
    public Optional<List<VacancyDto>> getVacanciesByCategory(Integer categoryId) {
        List<VacancyDto> vacancies = vacancyDao.getVacanciesByCategory(categoryId);
        return vacancies.isEmpty() ? Optional.empty() : Optional.of(vacancies);
    }
}
