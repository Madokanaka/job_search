package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Optional<List<VacancyDto>> getVacanciesUserRespondedTo(Integer userId) {
        List<VacancyDto> vacancies = vacancyDao.getVacanciesUserRespondedTo(userId);
        return vacancies.isEmpty() ? Optional.empty() : Optional.of(vacancies);
    }

    @Override
    public Optional<List<UserDto>> getApplicantsForVacancy(Integer vacancyId) {
        List<User> applicants = vacancyDao.getApplicantsForVacancy(vacancyId);

        List<UserDto> userDtos = applicants.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getSurname(),
                        user.getAge(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getPhoneNumber(),
                        user.getAvatar(),
                        user.getAccountType()
                ))
                .collect(Collectors.toList());

        return userDtos.isEmpty() ? Optional.empty() : Optional.of(userDtos);
    }
}
