package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.CategoryDao;
import kg.attractor.job_search.dao.VacancyDao;
import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.exception.VacancyNotFoundException;
import kg.attractor.job_search.model.Category;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyServiceImpl implements VacancyService {

    private final VacancyDao vacancyDao;
    private final CategoryDao categoryDao;

    @Override
    public void createVacancy(VacancyDto vacancyDto, Integer userId) {
        log.info("Creating vacancy for user with ID {}", userId);

        if (!vacancyDao.existsUserById(userId)) {
            log.warn("User with ID {} not found", userId);
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        if (!vacancyDao.isUserEmployer(userId)) {
            log.warn("User with ID {} is not an employer", userId);
            throw new BadRequestException("User with id " + userId + " is not employer");
        }

        if (!vacancyDao.existsCategoryById(vacancyDto.getCategoryId())) {
            log.warn("Category with ID {} not found", vacancyDto.getCategoryId());
            throw new ResourceNotFoundException("Category with id " + vacancyDto.getCategoryId() + " not found");
        }

        if (vacancyDto.getExpFrom() >= vacancyDto.getExpTo() && vacancyDto.getExpFrom() != 0) {
            log.warn("Invalid experience range: from {} to {}", vacancyDto.getExpFrom(), vacancyDto.getExpTo());
            throw new BadRequestException("Exp to should be greater than experience from");
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
        log.info("Vacancy created successfully for user with ID {}", userId);
    }

    @Override
    public void editVacancy(Integer vacancyId, VacancyDto vacancyDto) {
        log.info("Editing vacancy with ID {}", vacancyId);

        Optional<Vacancy> optionalVacancy = vacancyDao.getVacancyById(vacancyId);

        if (optionalVacancy.isEmpty()) {
            log.warn("Vacancy with ID {} not found", vacancyId);
            throw new ResourceNotFoundException("Vacancy with id " + vacancyId + " not found");
        }

        if (vacancyDto.getExpFrom() >= vacancyDto.getExpTo() && vacancyDto.getExpFrom() != 0) {
            log.warn("Invalid experience range for vacancy edit: from {} to {}", vacancyDto.getExpFrom(), vacancyDto.getExpTo());
            throw new BadRequestException("Exp to should be greater than experience from");
        }

        Vacancy vacancy = optionalVacancy.get();
        vacancy.setDescription(vacancyDto.getDescription());
        vacancy.setName(vacancyDto.getName());
        vacancy.setSalary(vacancyDto.getSalary());
        vacancy.setExpFrom(vacancyDto.getExpFrom());
        vacancy.setExpTo(vacancyDto.getExpTo());
        vacancy.setIsActive(vacancyDto.getIsActive());

        vacancyDao.updateVacancy(vacancyId, vacancy);
        log.info("Vacancy with ID {} updated successfully", vacancyId);
    }

    @Override
    public void deleteVacancy(Integer vacancyId) {
        log.info("Deleting vacancy with ID {}", vacancyId);
        vacancyDao.deleteVacancy(vacancyId);
        log.info("Vacancy with ID {} deleted successfully", vacancyId);
    }

    @Override
    public List<VacancyDto> getAllVacancies() {
        log.info("Retrieving all vacancies");
        List<Vacancy> vacancies = vacancyDao.getAllVacancies();

        if (vacancies.isEmpty()) {
            log.warn("No vacancies found");
            throw new ResourceNotFoundException("No vacancies found");
        }

        log.info("Retrieved {} vacancies", vacancies.size());
        return vacancies.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<List<VacancyDto>> getVacanciesByUserId(Integer userId) {
        log.info("Retrieving all vacancies");
        List<Vacancy> vacancies = vacancyDao.getVacanciesByUserId(userId);

        log.info("Retrieved {} vacancies", vacancies.size());
        return Optional.of(vacancies.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<VacancyDto>> getVacanciesByCategory(Integer categoryId) {
        log.info("Retrieving vacancies by category ID {}", categoryId);

        if (categoryId == null || categoryId <= 0) {
            log.warn("Invalid category ID: {}", categoryId);
            throw new BadRequestException("Invalid category ID");
        }

        if (!vacancyDao.existsCategoryById(categoryId)) {
            log.warn("Category with ID {} not found", categoryId);
            throw new ResourceNotFoundException("Category not found in database");
        }

        List<VacancyDto> vacancies = vacancyDao.getVacanciesByCategory(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        if (vacancies.isEmpty()) {
            log.warn("No vacancies found for category ID {}", categoryId);
            throw new VacancyNotFoundException("Vacancies with category " + categoryId + " were not found");
        }

        log.info("Retrieved {} vacancies for category ID {}", vacancies.size(), categoryId);
        return Optional.of(vacancies);
    }

    @Override
    public Optional<List<VacancyDto>> getVacanciesUserRespondedTo(Integer userId) {
        log.info("Retrieving vacancies responded to by user ID {}", userId);

        if (userId == null || userId <= 0) {
            log.warn("Invalid user ID: {}", userId);
            throw new BadRequestException("Invalid user ID");
        }

        if (!vacancyDao.existsUserById(userId)) {
            log.warn("User with ID {} not found", userId);
            throw new UserNotFoundException("User not found in database");
        }

        List<VacancyDto> vacancies = vacancyDao.getVacanciesUserRespondedTo(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("Found {} vacancies responded to by user ID {}", vacancies.size(), userId);
        return vacancies.isEmpty() ? Optional.empty() : Optional.of(vacancies);
    }

    @Override
    public Optional<VacancyDto> getVacancyById(Integer vacancyId) {
        log.info("Retrieving vacancy by ID {}", vacancyId);

        if (vacancyId == null || vacancyId <= 0) {
            log.warn("Invalid vacancy ID: {}", vacancyId);
            throw new BadRequestException("Invalid vacancy ID: " + vacancyId);
        }

        Optional<Vacancy> vacancy = vacancyDao.getVacancyById(vacancyId);

        if (vacancy.isEmpty()) {
            log.warn("Vacancy with ID {} not found", vacancyId);
            throw new VacancyNotFoundException("Vacancy with ID " + vacancyId + " not found");
        }

        log.info("Vacancy with ID {} retrieved successfully", vacancyId);
        return vacancy.map(this::convertToDto);
    }

    private VacancyDto convertToDto(Vacancy vacancy) {
        return VacancyDto.builder()
                .id(vacancy.getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .categoryId(vacancy.getCategoryId())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .build();
    }

    public String getCategoryName(Integer categoryId) {
        return categoryDao.getCategoryById(categoryId).getName();
    }

    public List<Category> getCategories() {
        return categoryDao.getAllCategories();
    }
}
