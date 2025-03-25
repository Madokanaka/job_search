package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.Vacancy;
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
    public void createVacancy(VacancyDto vacancyDto, Integer userId) {
        if (!vacancyDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        }

        if (!vacancyDao.isUserEmployer(userId)) {
            throw new ResourceNotFoundException("User with id " + userId + " is not employer");
        }

        if (vacancyDao.existsCategoryById(vacancyDto.getCategoryId())) {
            throw new ResourceNotFoundException("Category with id " + vacancyDto.getCategoryId() + " not found");
        }
        Vacancy vacancy = new Vacancy().builder()
                .authorId(userId)
                .categoryId(vacancyDto.getCategoryId())
                .isActive(true)
                .description(vacancyDto.getDescription())
                .name(vacancyDto.getName())
                .salary(vacancyDto.getSalary())
                .expFrom(vacancyDto.getExpFrom())
                .expTo(vacancyDto.getExpTo())
                .build();

        vacancyDao.createVacancy(vacancy);
    }

    @Override
    public VacancyDto editVacancy(Integer vacancyId, VacancyDto vacancyDto) {
        if (vacancyDao.getVacancyById(vacancyId).isPresent()) {
            throw new ResourceNotFoundException("Vacancy with id " + vacancyId + " not found");
        }
        Optional<Vacancy> optionalVacancy= vacancyDao.getVacancyById(vacancyId);

        Vacancy vacancy = optionalVacancy.get();
        vacancy.setDescription(vacancyDto.getDescription());
        vacancy.setName(vacancyDto.getName());
        vacancy.setSalary(vacancyDto.getSalary());
        vacancy.setExpFrom(vacancyDto.getExpFrom());
        vacancy.setExpTo(vacancyDto.getExpTo());
        vacancy.setIsActive(vacancyDto.getIsActive());

        vacancyDao.updateVacancy(vacancyId, vacancy);
        return vacancyDto;
        }

    @Override
    public void deleteVacancy(Integer vacancyId) {
        vacancyDao.deleteVacancy(vacancyId);
    }

    @Override
    public List<VacancyDto> getAllVacancies() {
        List<Vacancy> vacancies = vacancyDao.getAllVacancies();
        if (vacancies.isEmpty()) {
            throw new ResourceNotFoundException("No vacancies found");
        }
        return vacancies.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<List<VacancyDto>> getVacanciesByCategory(Integer categoryId) {
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("Invalid category ID");
        }

        if (!vacancyDao.existsCategoryById(categoryId)) {
            throw new ResourceNotFoundException("Category not found in database");
        }

        List<VacancyDto> vacancies = vacancyDao.getVacanciesByCategory(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return vacancies.isEmpty() ? Optional.empty() : Optional.of(vacancies);
    }

    @Override
    public Optional<List<VacancyDto>> getVacanciesUserRespondedTo(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (!vacancyDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("User not found in database");
        }

        List<VacancyDto> vacancies = vacancyDao.getVacanciesUserRespondedTo(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

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

    @Override
    public Optional<VacancyDto> getVacancyById(Integer vacancyId) {
        if (vacancyId == null || vacancyId <= 0) {
            throw new IllegalArgumentException("Invalid vacancy ID: " + vacancyId);
        }

        Optional<Vacancy> vacancy = vacancyDao.getVacancyById(vacancyId);

        if (vacancy.isEmpty()) {
            throw new ResourceNotFoundException("Vacancy with ID " + vacancyId + " not found");
        }

        return vacancy.map(this::convertToDto);
    }

    private VacancyDto convertToDto(Vacancy vacancy) {
        return VacancyDto.builder()
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .categoryId(vacancy.getCategoryId())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .build();
    }

}
