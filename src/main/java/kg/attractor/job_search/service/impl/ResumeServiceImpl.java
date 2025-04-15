package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.CategoryDao;
import kg.attractor.job_search.dao.ContactInfoDao;
import kg.attractor.job_search.dao.ContactTypeDao;
import kg.attractor.job_search.dao.EducationInfoDao;
import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dao.WorkExperienceInfoDao;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.model.Category;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeDao resumeDao;
    private final WorkExperienceInfoDao workExperienceInfoDao;
    private final EducationInfoDao educationInfoDao;
    private final ContactInfoDao contactInfoDao;
    private final ContactTypeDao contactTypeDao;
    private final CategoryDao categoryDao;

    @Override
    @Transactional
    public void createResume(ResumeDto resumeDto, Integer userId) {
        log.info("Creating resume for userId={}", userId);

        if (resumeDto == null || userId == null || userId <= 0) {
            log.warn("Invalid resumeDto or userId");
            throw new BadRequestException("User ID or resume have invalid values");
        }

        if (!resumeDao.existsApplicantById(userId)) {
            log.warn("Applicant with ID={} not found", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found in database");
        }

        if (!resumeDao.existsCategoryById(resumeDto.getCategoryId())) {
            log.warn("Category ID={} not found", resumeDto.getCategoryId());
            throw new ResourceNotFoundException("Category ID not found in database");
        }

        Resume resume = new Resume();
        resume.setApplicantId(userId);
        resume.setName(resumeDto.getName());
        resume.setCategoryId(resumeDto.getCategoryId());
        resume.setSalary(resumeDto.getSalary());
        resume.setIsActive(true);
        resume.setCreated_date(LocalDateTime.now());
        resume.setUpdate_time(LocalDateTime.now());

        int resumeId = resumeDao.createResume(resume);

        if (resumeId == -1) {
            log.error("Failed to create resume");
            throw new DatabaseOperationException("Could not create resume");
        }

        log.debug("Resume created with id={}", resumeId);
        saveResumeDetails(resumeId, resumeDto);
    }

    @Override
    @Transactional
    public void deleteResume(Integer resumeId) {
        log.info("Deleting resume with id={}", resumeId);

        if (resumeDao.findById(resumeId).isEmpty()) {
            log.warn("Resume with id={} not found", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }

        contactInfoDao.deleteContactInfoByResumeId(resumeId);
        educationInfoDao.deleteEducationInfoByResumeId(resumeId);
        workExperienceInfoDao.deleteWorkExperienceInfoByResumeId(resumeId);

        if (!resumeDao.deleteResume(resumeId)) {
            log.error("Could not delete resume with id={}", resumeId);
            throw new DatabaseOperationException("Could not delete resume");
        }

        log.debug("Resume with id={} deleted successfully", resumeId);
    }

    @Override
    @Transactional
    public void editResume(Integer resumeId, ResumeDto resumeDto) {
        log.info("Editing resume with id={}", resumeId);

        Optional<Resume> optionalResume = resumeDao.findById(resumeId);
        if (optionalResume.isEmpty()) {
            log.warn("Resume with id={} not found", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }

        Resume resume = optionalResume.get();
        resume.setName(resumeDto.getName());
        resume.setSalary(resumeDto.getSalary());
        resume.setIsActive(resumeDto.getIsActive());
        resume.setUpdate_time(LocalDateTime.now());

        if (resumeDao.updateResume(resume)) {
            log.debug("Resume with id={} updated", resume.getId());

            contactInfoDao.deleteContactInfoByResumeId(resume.getId());
            educationInfoDao.deleteEducationInfoByResumeId(resume.getId());
            workExperienceInfoDao.deleteWorkExperienceInfoByResumeId(resume.getId());

            saveResumeDetails(resume.getId(), resumeDto);
        } else {
            log.error("Could not update resume with id={}", resume.getId());
            throw new DatabaseOperationException("Could not update resume");
        }
    }

    @Override
    public List<ResumeDto> getAllResumes() {
        log.info("Fetching all resumes");
        return resumeDao.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<List<ResumeDto>> getResumesByUserId(Integer userId) {
        log.info("Fetching resumes for userId={}", userId);

        if (userId == null || userId <= 0) {
            log.warn("Invalid userId={}", userId);
            throw new BadRequestException("User ID has invalid value");
        }

        if (!resumeDao.existsApplicantById(userId)) {
            log.warn("Applicant with id={} not found", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found in database");
        }

        return resumeDao.findByUserId(userId).map(resumes -> resumes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    private void saveResumeDetails(Integer resumeId, ResumeDto resumeDto) {
        log.info("Saving resume details for resumeId={}", resumeId);

        if (resumeDto.getContactEmail() != null && !resumeDto.getContactEmail().isBlank()) {
            resumeDao.createContactInfo(resumeId, contactTypeDao.getContactTypeId("Email"), resumeDto.getContactEmail());
        }

        if (resumeDto.getPhoneNumber() != null && !resumeDto.getPhoneNumber().isBlank()) {
            resumeDao.createContactInfo(resumeId, contactTypeDao.getContactTypeId("Телефон"), resumeDto.getPhoneNumber());
        }

        if (resumeDto.getLinkedIn() != null && !resumeDto.getLinkedIn().isBlank()) {
            resumeDao.createContactInfo(resumeId, contactTypeDao.getContactTypeId("LinkedIn"), resumeDto.getLinkedIn());
        }

        if (resumeDto.getTelegram() != null && !resumeDto.getTelegram().isBlank()) {
            resumeDao.createContactInfo(resumeId, contactTypeDao.getContactTypeId("Telegram"), resumeDto.getTelegram());
        }

        if (resumeDto.getFacebook() != null && !resumeDto.getFacebook().isBlank()) {
            resumeDao.createContactInfo(resumeId, contactTypeDao.getContactTypeId("Facebook"), resumeDto.getFacebook());
        }

        if (resumeDto.getEducationInfoList() != null) {
            resumeDto.getEducationInfoList().forEach(educationInfoDto -> {
                if (educationInfoDto.getStartDate().isAfter(educationInfoDto.getEndDate())) {
                    log.warn("Invalid education dates: startDate={} > endDate={}", educationInfoDto.getStartDate(), educationInfoDto.getEndDate());
                    throw new BadRequestException("Start date cannot be after end date");
                }
                EducationInfo educationInfo = new EducationInfo();
                educationInfo.setResumeId(resumeId);
                educationInfo.setInstitution(educationInfoDto.getInstitution());
                educationInfo.setProgram(educationInfoDto.getProgram());
                educationInfo.setStartDate(educationInfoDto.getStartDate());
                educationInfo.setEndDate(educationInfoDto.getEndDate());
                educationInfo.setDegree(educationInfoDto.getDegree());
                educationInfoDao.createEducationInfo(educationInfo);
            });
        }

        if (resumeDto.getWorkExperienceInfoList() != null) {
            resumeDto.getWorkExperienceInfoList().forEach(workExperienceInfoDto -> {
                WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
                workExperienceInfo.setResumeId(resumeId);
                workExperienceInfo.setYears(workExperienceInfoDto.getYears());
                workExperienceInfo.setCompanyName(workExperienceInfoDto.getCompanyName());
                workExperienceInfo.setPosition(workExperienceInfoDto.getPosition());
                workExperienceInfo.setResponsibilities(workExperienceInfoDto.getResponsibilities());
                workExperienceInfoDao.createWorkExperienceInfo(workExperienceInfo);
            });
        }

        log.debug("Resume details saved for resumeId={}", resumeId);
    }

    @Override
    public Map<Integer, String> getCategories() {
        List<Category> categoryList = categoryDao.getAllCategories();
        Map<Integer, String> categoryMap = categoryList.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        return categoryMap;
    }

    private ResumeDto convertToDto(Resume resume) {
        List<ContactInfo> contactInfos = contactInfoDao.findContactInfoByResumeId(resume.getId());

        String email = getValueByTypeKey(contactInfos, "email");
        String phone = getValueByTypeKey(contactInfos, "Телефон");
        String linkedIn = getValueByTypeKey(contactInfos, "linkedin");
        String telegram = getValueByTypeKey(contactInfos, "telegram");
        String facebook = getValueByTypeKey(contactInfos, "facebook");

        return ResumeDto.builder()
                .id(resume.getId())
                .name(resume.getName())
                .categoryId(resume.getCategoryId())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .update_time(resume.getUpdate_time())
                .created_date(resume.getCreated_date())
                .contactEmail(email)
                .phoneNumber(phone)
                .linkedIn(linkedIn)
                .telegram(telegram)
                .facebook(facebook)
                .educationInfoList(educationInfoDao.findEducationInfoByResumeId(resume.getId()).stream()
                        .map(ei -> EducationInfoDto.builder()
                                .institution(ei.getInstitution())
                                .program(ei.getProgram())
                                .startDate(ei.getStartDate())
                                .endDate(ei.getEndDate())
                                .degree(ei.getDegree())
                                .build())
                        .collect(Collectors.toList()))
                .workExperienceInfoList(workExperienceInfoDao.findWorkExperienceInfoByResumeId(resume.getId()).stream()
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
                .filter(ci -> {
                    Integer typeId = contactTypeDao.getContactTypeId(typeKey);
                    return typeId != null && ci.getTypeId().equals(typeId);
                })
                .map(ContactInfo::getValue)
                .findFirst()
                .orElse(null);
    }
}
