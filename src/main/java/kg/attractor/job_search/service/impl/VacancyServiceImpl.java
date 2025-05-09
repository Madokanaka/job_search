package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.VacancyDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.exception.VacancyNotFoundException;
import kg.attractor.job_search.model.Category;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.Vacancy;
import kg.attractor.job_search.repository.VacancyRepository;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;

    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public void createVacancy(VacancyDto vacancyDto, Integer userId) {
        validateExperienceRange(vacancyDto);
        log.info("Creating vacancy for user with ID {}", userId);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        if (!"employer".equalsIgnoreCase(user.getAccountType())) {
            log.warn("User with ID {} is not an employer", userId);
            throw new BadRequestException("User with id " + userId + " is not employer");
        }

        Category category = categoryService.findById(vacancyDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + vacancyDto.getCategoryId() + " not found"));

        if (vacancyDto.getExpFrom() >= vacancyDto.getExpTo() && vacancyDto.getExpFrom() != 0) {
            log.warn("Invalid experience range: from {} to {}", vacancyDto.getExpFrom(), vacancyDto.getExpTo());
            throw new BadRequestException("Exp to should be greater than experience from");
        }

        Vacancy vacancy = Vacancy.builder()
                .author(user)
                .category(category)
                .isActive(true)
                .description(vacancyDto.getDescription())
                .name(vacancyDto.getName())
                .salary(vacancyDto.getSalary())
                .expFrom(vacancyDto.getExpFrom())
                .expTo(vacancyDto.getExpTo())
                .updateTime(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();

        vacancyRepository.save(vacancy);
        log.info("Vacancy created successfully for user with ID {}", userId);
    }

    @Override
    public void editVacancy(Integer vacancyId, VacancyDto vacancyDto) {
        validateExperienceRange(vacancyDto);
        log.info("Editing vacancy with ID {}", vacancyId);

        Optional<Vacancy> optionalVacancy = vacancyRepository.findById(vacancyId);

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
        vacancy.setUpdateTime(LocalDateTime.now());

        vacancyRepository.save(vacancy);
        log.info("Vacancy with ID {} updated successfully", vacancyId);
    }

    @Override
    public void deleteVacancy(Integer vacancyId) {
        log.info("Deleting vacancy with ID {}", vacancyId);
        vacancyRepository.deleteById(vacancyId);
        log.info("Vacancy with ID {} deleted successfully", vacancyId);
    }

    @Override
    public List<VacancyDto> getAllVacancies() {
        log.info("Retrieving all vacancies");
        List<Vacancy> vacancies = vacancyRepository.findAll();

        if (vacancies.isEmpty()) {
            log.warn("No vacancies found");
            throw new ResourceNotFoundException("No vacancies found");
        }

        log.info("Retrieved {} vacancies", vacancies.size());
        return vacancies.stream()
                .sorted(Comparator.comparing(Vacancy::getUpdateTime, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<List<VacancyDto>> getVacanciesByUserId(Integer userId) {
        log.info("Retrieving all vacancies for user with ID {}", userId);
        List<Vacancy> vacancies = vacancyRepository.findByAuthorId(userId);

        log.info("Retrieved {} vacancies for user", vacancies.size());
        return Optional.of(vacancies.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @Override
    public Optional<List<VacancyDto>> getVacanciesByCategory(Integer categoryId) {
        log.info("Retrieving vacancies by category ID {}", categoryId);

        if (categoryId == null || categoryId <= 0) {
            log.warn("Invalid category ID: {}", categoryId);
            throw new BadRequestException("Invalid category ID");
        }

        if (!categoryService.existsByCategoryId(categoryId)) {
            log.warn("Category with ID {} not found", categoryId);
            throw new ResourceNotFoundException("Category not found in database");
        }

        List<VacancyDto> vacancies = vacancyRepository.findByCategoryId(categoryId).stream()
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

        List<VacancyDto> vacancies = vacancyRepository.findVacanciesUserRespondedTo(userId).stream()
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

        Optional<Vacancy> vacancy = vacancyRepository.findById(vacancyId);

        if (vacancy.isEmpty()) {
            log.warn("Vacancy with ID {} not found", vacancyId);
            throw new VacancyNotFoundException("Vacancy with ID " + vacancyId + " not found");
        }

        log.info("Vacancy with ID {} retrieved successfully", vacancyId);
        return vacancy.map(this::convertToDto);
    }

    @Override
    public Vacancy getVacancyModelById(Integer vacancyId) {
        log.info("Retrieving vacancy by ID {}", vacancyId);

        if (vacancyId == null || vacancyId <= 0) {
            log.warn("Invalid vacancy ID: {}", vacancyId);
            throw new BadRequestException("Invalid vacancy ID: " + vacancyId);
        }

        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow(() -> new VacancyNotFoundException("Vacancy with ID " + vacancyId + " not found"));


        log.info("Vacancy with ID {} retrieved successfully", vacancyId);
        return vacancy;
    }

    private VacancyDto convertToDto(Vacancy vacancy) {
        return VacancyDto.builder()
                .id(vacancy.getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .categoryId(vacancy.getCategory().getId())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .createdDate(vacancy.getCreatedDate())
                .updateTime(vacancy.getUpdateTime())
                .authorId(vacancy.getAuthor().getId())
                .categoryName(categoryService.getCategoryNameById(vacancy.getCategory().getId()))
                .build();
    }

    @Override
    public Page<VacancyDto> getAllVacanciesPaged(String pageNumber, String pageSize, String sortType) {
        int page = parsePageParameter(pageNumber);
        int size = parseSizeParameter(pageSize, 6);

        Sort sort = Sort.by("updateTime").descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Vacancy> vacanciesPage;

        if ("responses".equals(sortType)) {
            vacanciesPage = vacancyRepository.findAllOrderByResponseCount(pageable);
        } else {
            vacanciesPage = vacancyRepository.findAll(pageable);
        }

        if (page >= vacanciesPage.getTotalPages()) {
            pageable = PageRequest.of(vacanciesPage.getTotalPages() - 1, size, sort);
            vacanciesPage = "responses".equals(sortType) ?
                    vacancyRepository.findAllOrderByResponseCount(pageable) :
                    vacancyRepository.findAll(pageable);
        }

        List<VacancyDto> vacancyDtos = vacanciesPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(vacancyDtos, pageable, vacanciesPage.getTotalElements());
    }


    @Override
    public Page<VacancyDto> getVacanciesByUserIdPaged(Integer userId, String pageNumber, String pageSize) {
        int page = parsePageParameter(pageNumber);
        int size = parseSizeParameter(pageSize, 6);

        Pageable pageable = PageRequest.of(page, size, Sort.by("updateTime").descending());
        Page<Vacancy> vacanciesPage = vacancyRepository.findByAuthorId(userId, pageable);
        if (vacanciesPage.getTotalPages() > 0 && page >= vacanciesPage.getTotalPages()) {
            log.warn("Запрашиваемая страница больше максимальной, выбираем последнюю страницу");
            pageable = PageRequest.of(vacanciesPage.getTotalPages() - 1, size, Sort.by("updateTime").descending());
            vacanciesPage = vacancyRepository.findByAuthorId(userId, pageable);
        }

        List<VacancyDto> vacancyDtos = vacanciesPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(vacancyDtos, pageable, vacanciesPage.getTotalElements());
    }


    private int parsePageParameter(String page) {
        try {
            int pageNumber = Integer.parseInt(page);
            if (pageNumber < 0) {
                log.warn("Page index less than 0, setting to 0");
                return 0;
            }
            return pageNumber;
        } catch (NumberFormatException e) {
            log.warn("Invalid page parameter: {}. Setting to default 0", page);
            return 0;
        }
    }

    private int parseSizeParameter(String size, int defaultValue) {
        try {
            int pageSize = Integer.parseInt(size);
            if (pageSize <= 0 || pageSize > 100) {
                log.warn("Invalid size parameter: {}. Setting to default 6", size);
                return defaultValue;
            }
            return pageSize;
        } catch (NumberFormatException e) {
            log.warn("Invalid size parameter: {}. Setting to default 6", size);
            return defaultValue;
        }
    }

    private void validateExperienceRange(VacancyDto vacancyDto) {
        if (vacancyDto.getExpTo() < vacancyDto.getExpFrom()) {
            throw new IllegalArgumentException("Experience 'to' must be greater than or equal to 'from'");
        }
    }

    @Override
    public int getCategoryIdByVacancyId(Integer vacancyId) {
        Vacancy vacancy = getVacancyModelById(vacancyId);

        if (vacancy.getCategory() == null) {
            throw new ResourceNotFoundException("Vacancy category is not set for id " + vacancyId);
        }

        return vacancy.getCategory().getId();
    }
}
