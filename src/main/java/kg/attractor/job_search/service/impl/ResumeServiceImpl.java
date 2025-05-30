package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.*;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.model.*;
import kg.attractor.job_search.repository.*;
import kg.attractor.job_search.service.CategoryService;
import kg.attractor.job_search.service.ContactInfoService;
import kg.attractor.job_search.service.EducationInfoService;
import kg.attractor.job_search.service.ResumeService;
import kg.attractor.job_search.service.UserService;
import kg.attractor.job_search.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;

    private final UserService userService;
    private final CategoryService categoryService;
    private final ContactInfoService contactInfoService;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final MessageSource messageSource;


    @Override
    @Transactional
    public void createResume(ResumeDto resumeDto, Integer userId) {
        log.info("Creating resume for userId={}", userId);

        if (resumeDto == null || userId == null || userId <= 0) {
            log.warn("Invalid resumeDto or userId");
            throw new BadRequestException(messageSource.getMessage("error.invalid.resume.or.user", null, LocaleContextHolder.getLocale()));
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("error.user.not.found", new Object[]{userId}, LocaleContextHolder.getLocale())));

        Category category = categoryService.findById(resumeDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage("error.category.not.found", new Object[]{resumeDto.getCategoryId()}, LocaleContextHolder.getLocale())));

        Resume resume = new Resume();
        resume.setApplicant(user);
        resume.setName(resumeDto.getName());
        resume.setCategory(category);
        resume.setSalary(resumeDto.getSalary());
        resume.setIsActive(true);
        resume.setCreatedDate(LocalDateTime.now());
        resume.setUpdateTime(LocalDateTime.now());

        resume = resumeRepository.save(resume);
        log.debug("Resume created with id={}", resume.getId());

        contactInfoService.createContactInfo(resumeDto, resume);
        educationInfoService.createEducationInfo(resumeDto.getEducationInfoList(), resume);
        workExperienceInfoService.createWorkExperienceInfo(resumeDto.getWorkExperienceInfoList(), resume);
    }

    @Override
    @Transactional
    public void deleteResume(Integer resumeId) {
        log.info("Deleting resume with id={}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException(messageSource.getMessage("error.resume.not.found", new Object[]{resumeId}, LocaleContextHolder.getLocale())));

        contactInfoService.deleteContactInfoByResumeId(resumeId);
        educationInfoService.deleteEducationInfoByResumeId(resumeId);
        workExperienceInfoService.deleteWorkExperienceInfoByResumeId(resumeId);

        resumeRepository.delete(resume);
        log.debug("Resume with id={} deleted successfully", resumeId);
    }

    @Override
    @Transactional
    public void editResume(Integer resumeId, ResumeDto resumeDto) {
        log.info("Editing resume with id={}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException(messageSource.getMessage("error.resume.not.found", new Object[]{resumeId}, LocaleContextHolder.getLocale())));

        resume.setName(resumeDto.getName());
        resume.setSalary(resumeDto.getSalary());
        resume.setIsActive(true);
        resume.setUpdateTime(LocalDateTime.now());

        resumeRepository.save(resume);
        log.debug("Resume with id={} updated", resume.getId());

        contactInfoService.deleteContactInfoByResumeId(resumeId);
        educationInfoService.deleteEducationInfoByResumeId(resumeId);
        workExperienceInfoService.deleteWorkExperienceInfoByResumeId(resumeId);

        contactInfoService.createContactInfo(resumeDto, resume);
        educationInfoService.createEducationInfo(resumeDto.getEducationInfoList(), resume);
        workExperienceInfoService.createWorkExperienceInfo(resumeDto.getWorkExperienceInfoList(), resume);
    }

    @Override
    public ResumeDto getResumeById(Integer resumeId) {
        log.info("Fetching resume with id={}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException(messageSource.getMessage("error.resume.not.found", new Object[]{resumeId}, LocaleContextHolder.getLocale())));

        return convertToDto(resume);
    }

    @Override
    public Resume getResumeModelById(Integer resumeId) {
        log.info("Fetching resume with id={}", resumeId);

        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException(messageSource.getMessage("error.resume.not.found", new Object[]{resumeId}, LocaleContextHolder.getLocale())));
    }

    @Override
    public List<ResumeDto> getAllResumes() {
        log.info("Fetching all resumes");
        return resumeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<List<ResumeDto>> getResumesByUserId(Integer userId) {
        log.info("Fetching resumes for userId={}", userId);

        if (userId == null || userId <= 0) {
            log.warn("Invalid userId={}", userId);
            throw new BadRequestException(messageSource.getMessage("error.invalid.userId", null, LocaleContextHolder.getLocale()));
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("error.user.not.found", null, LocaleContextHolder.getLocale())));

        List<Resume> resumes = resumeRepository.findByApplicantId(user.getId());

        return Optional.of(resumes.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @Override
    public Map<Integer, String> getCategories() {
        return categoryService.findAll().stream()
                .collect(Collectors.toMap(CategoryDto::getId, CategoryDto::getName));
    }


    private ResumeDto convertToDto(Resume resume) {
        List<ContactInfo> contactInfos = contactInfoService.getContactInfoByResumeId(resume.getId());

        String email = getValueByTypeKey(contactInfos, "Email");
        String phone = getValueByTypeKey(contactInfos, "Телефон");
        String linkedIn = getValueByTypeKey(contactInfos, "LinkedIn");
        String telegram = getValueByTypeKey(contactInfos, "Telegram");
        String facebook = getValueByTypeKey(contactInfos, "Facebook");

        return ResumeDto.builder()
                .id(resume.getId())
                .name(resume.getName())
                .categoryId(resume.getCategory().getId())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .update_time(resume.getUpdateTime())
                .created_date(resume.getCreatedDate())
                .contactEmail(email)
                .authorId(resume.getApplicant().getId())
                .phoneNumber(phone)
                .linkedIn(linkedIn)
                .telegram(telegram)
                .facebook(facebook)
                .educationInfoList(new ArrayList<>(educationInfoService.getEducationInfoByResumeId(resume.getId())))
                .workExperienceInfoList(new ArrayList<>(workExperienceInfoService.getWorkExperienceInfoByResumeId(resume.getId())))
                .categoryName(resume.getCategory().getName())
                .build();
    }

    private String getValueByTypeKey(List<ContactInfo> contactInfos, String typeKey) {
        return contactInfos.stream()
                .filter(ci -> ci.getType().getType().equals(typeKey))
                .map(ContactInfo::getInfoValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Page<ResumeDto> getAllResumesPaged(String pageNumber, String pageSize) {
        int page = parsePageParameter(pageNumber);
        int size = parseSizeParameter(pageSize, 9);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Resume> resumes = resumeRepository.findAll(pageable);
        if (page > resumes.getTotalPages()) {
            pageable = PageRequest.of(resumes.getTotalPages() - 1, size, Sort.by("createdDate").descending());
            resumes = resumeRepository.findAll(pageable);
        }
        return resumes
                .map(this::convertToDto);
    }

    @Override
    public Page<ResumeDto> getResumesByUserIdPaged(Integer userId, String pageNumber, String pageSize) {
        int page = parsePageParameter(pageNumber);
        int size = parseSizeParameter(pageSize, 6);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Resume> resumePage = resumeRepository.findByApplicantId(userId, pageable);
        if (resumePage.getTotalPages() > 0 && page >= resumePage.getTotalPages()) {
            log.warn("Запрашиваемая страница больше максимальной, выбираем последнюю страницу");
            pageable = PageRequest.of(resumePage.getTotalPages() - 1, size, Sort.by("updateTime").descending());
            resumePage = resumeRepository.findByApplicantId(userId, pageable);
        }

        List<ResumeDto> resumeDtos = resumePage.getContent().stream()
                .map(this::convertToDto)
                .toList();
        return new PageImpl<>(resumeDtos, pageable, resumePage.getTotalElements());
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

    @Override
    public int getCategoryIdByResumeId(Integer resumeId) {
        Resume resume = getResumeModelById(resumeId);

        if (resume.getCategory() == null) {
            throw new ResourceNotFoundException(messageSource.getMessage("error.resume.category.not.found", new Object[]{resumeId}, LocaleContextHolder.getLocale()));
        }

        return resume.getCategory().getId();
    }

    @Override
    public List<ResumeDto> getResumesByUserAndCategory(org.springframework.security.core.userdetails.User principal, Integer categoryId) {
        log.info("Fetching resumes for userId={} and categoryId={}", principal.getUsername(), categoryId);
        if (principal == null) {
            throw new BadRequestException("User is not authenticated");
        }

        if (categoryService.findById(categoryId).isEmpty()) {
            throw new BadRequestException(messageSource.getMessage("error.category.not.found", new Object[]{categoryId}, LocaleContextHolder.getLocale()));
        }

        Optional<UserDto> user = userService.findUserByEmail(principal.getUsername());

        if (user.isEmpty()) {
            throw new BadRequestException(messageSource.getMessage("error.user.not.found", new Object[]{principal.getUsername()}, LocaleContextHolder.getLocale()));
        }

        List<Resume> resumes = resumeRepository.findByApplicantIdAndCategoryId(user.get().getId(), categoryId);
        return resumes.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
