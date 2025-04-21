package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.*;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.model.*;
import kg.attractor.job_search.repository.*;
import kg.attractor.job_search.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final EducationInfoRepository educationInfoRepository;
    private final WorkExperienceInfoRepository workExperienceInfoRepository;
    private final ContactTypeRepository contactTypeRepository;

    @Override
    @Transactional
    public void createResume(ResumeDto resumeDto, Integer userId) {
        log.info("Creating resume for userId={}", userId);

        if (resumeDto == null || userId == null || userId <= 0) {
            log.warn("Invalid resumeDto or userId");
            throw new BadRequestException("User ID or resume have invalid values");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        Category category = categoryRepository.findById(resumeDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category ID not found in database"));

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

        saveResumeDetails(resume.getId(), resumeDto, resume);
    }

    @Override
    @Transactional
    public void deleteResume(Integer resumeId) {
        log.info("Deleting resume with id={}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Resume with id " + resumeId + " not found"));

        contactInfoRepository.deleteByResumeId(resumeId);
        educationInfoRepository.deleteByResumeId(resumeId);
        workExperienceInfoRepository.deleteByResumeId(resumeId);

        resumeRepository.delete(resume);
        log.debug("Resume with id={} deleted successfully", resumeId);
    }

    @Override
    @Transactional
    public void editResume(Integer resumeId, ResumeDto resumeDto) {
        log.info("Editing resume with id={}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Resume with id " + resumeId + " not found"));

        resume.setName(resumeDto.getName());
        resume.setSalary(resumeDto.getSalary());
        resume.setIsActive(resumeDto.getIsActive());
        resume.setUpdateTime(LocalDateTime.now());

        resumeRepository.save(resume);
        log.debug("Resume with id={} updated", resume.getId());

        contactInfoRepository.deleteByResumeId(resumeId);
        educationInfoRepository.deleteByResumeId(resumeId);
        workExperienceInfoRepository.deleteByResumeId(resumeId);

        saveResumeDetails(resume.getId(), resumeDto, resume);
    }

    @Override
    public ResumeDto getResumeById(Integer resumeId) {
        log.info("Fetching resume with id={}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Resume with id " + resumeId + " not found"));

        return convertToDto(resume);
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
            throw new BadRequestException("User ID has invalid value");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found in database"));

        List<Resume> resumes = resumeRepository.findByApplicantId(user.getId());

        return Optional.of(resumes.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @Override
    public Map<Integer, String> getCategories() {
        return categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
    }

    private void saveResumeDetails(Integer resumeId, ResumeDto resumeDto, Resume resume) {
        log.info("Saving resume details for resumeId={}", resumeId);

        saveContactInfo(resumeId, resumeDto, resume);
        saveEducationInfo(resumeId, resumeDto, resume);
        saveWorkExperienceInfo(resumeId, resumeDto, resume);

        log.debug("Resume details saved for resumeId={}", resumeId);
    }

    private void saveContactInfo(Integer resumeId, ResumeDto resumeDto, Resume resume) {
        if (resumeDto.getContactEmail() != null && !resumeDto.getContactEmail().isBlank()) {
            ContactInfo contactInfo = new ContactInfo().builder()
                    .resume(resume)
                    .type(contactTypeRepository.findByType("Email"))
                    .infoValue(resumeDto.getContactEmail())
                    .build();
            contactInfoRepository.save(contactInfo);
        }

        if (resumeDto.getPhoneNumber() != null && !resumeDto.getPhoneNumber().isBlank()) {
            ContactInfo contactInfo = new ContactInfo().builder()
                    .resume(resume)
                    .type(contactTypeRepository.findByType("Телефон"))
                    .infoValue(resumeDto.getPhoneNumber())
                    .build();
            contactInfoRepository.save(contactInfo);
        }

        if (resumeDto.getLinkedIn() != null && !resumeDto.getLinkedIn().isBlank()) {
            ContactInfo contactInfo = new ContactInfo().builder()
                    .resume(resume)
                    .type(contactTypeRepository.findByType("LinkedIn"))
                    .infoValue(resumeDto.getLinkedIn())
                    .build();
            contactInfoRepository.save(contactInfo);
        }

        if (resumeDto.getTelegram() != null && !resumeDto.getTelegram().isBlank()) {
            ContactInfo contactInfo = new ContactInfo().builder()
                    .resume(resume)
                    .type(contactTypeRepository.findByType("Telegram"))
                    .infoValue(resumeDto.getTelegram())
                    .build();
            contactInfoRepository.save(contactInfo);
        }

        if (resumeDto.getFacebook() != null && !resumeDto.getFacebook().isBlank()) {
            ContactInfo contactInfo = new ContactInfo().builder()
                    .resume(resume)
                    .type(contactTypeRepository.findByType("Facebook"))
                    .infoValue(resumeDto.getFacebook())
                    .build();
            contactInfoRepository.save(contactInfo);
        }
    }

    private void saveEducationInfo(Integer resumeId, ResumeDto resumeDto, Resume resume) {
        if (resumeDto.getEducationInfoList() != null) {
            resumeDto.getEducationInfoList().forEach(educationInfoDto -> {
                EducationInfo educationInfo = new EducationInfo();
                educationInfo.setResume(resume);
                educationInfo.setInstitution(educationInfoDto.getInstitution());
                educationInfo.setProgram(educationInfoDto.getProgram());
                educationInfo.setStartDate(educationInfoDto.getStartDate());
                educationInfo.setEndDate(educationInfoDto.getEndDate());
                educationInfo.setDegree(educationInfoDto.getDegree());
                educationInfoRepository.save(educationInfo);
            });
        }
    }

    private void saveWorkExperienceInfo(Integer resumeId, ResumeDto resumeDto, Resume resume) {
        if (resumeDto.getWorkExperienceInfoList() != null) {
            resumeDto.getWorkExperienceInfoList().forEach(workExperienceInfoDto -> {
                WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
                workExperienceInfo.setResume(resume);
                workExperienceInfo.setCompanyName(workExperienceInfoDto.getCompanyName());
                workExperienceInfo.setPosition(workExperienceInfoDto.getPosition());
                workExperienceInfo.setYears(workExperienceInfoDto.getYears());
                workExperienceInfo.setResponsibilities(workExperienceInfoDto.getResponsibilities());
                workExperienceInfoRepository.save(workExperienceInfo);
            });
        }
    }

    private ResumeDto convertToDto(Resume resume) {
        List<ContactInfo> contactInfos = contactInfoRepository.findByResumeId(resume.getId());

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
                .phoneNumber(phone)
                .linkedIn(linkedIn)
                .telegram(telegram)
                .facebook(facebook)
                .educationInfoList(educationInfoRepository.findByResumeId(resume.getId()).stream()
                        .map(ei -> EducationInfoDto.builder()
                                .institution(ei.getInstitution())
                                .program(ei.getProgram())
                                .startDate(ei.getStartDate())
                                .endDate(ei.getEndDate())
                                .degree(ei.getDegree())
                                .build())
                        .collect(Collectors.toList()))
                .workExperienceInfoList(workExperienceInfoRepository.findByResumeId(resume.getId()).stream()
                        .map(wei -> WorkExperienceInfoDto.builder()
                                .companyName(wei.getCompanyName())
                                .position(wei.getPosition())
                                .years(wei.getYears())
                                .responsibilities(wei.getResponsibilities())
                                .build())
                        .collect(Collectors.toList()))
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
        return resumeRepository.findAll(pageable)
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
}
