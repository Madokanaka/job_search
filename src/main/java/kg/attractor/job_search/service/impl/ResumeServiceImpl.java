package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.*;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.service.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResumeServiceImpl implements ResumeService {

    private final ResumeDao resumeDao;

    public ResumeServiceImpl(ResumeDao resumeDao) {
        this.resumeDao = resumeDao;
    }

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

        resumeDao.deleteContactInfoByResumeId(resumeId);
        resumeDao.deleteEducationInfoByResumeId(resumeId);
        resumeDao.deleteWorkExperienceInfoByResumeId(resumeId);

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

            resumeDao.deleteContactInfoByResumeId(resume.getId());
            resumeDao.deleteEducationInfoByResumeId(resume.getId());
            resumeDao.deleteWorkExperienceInfoByResumeId(resume.getId());

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

        if (resumeDto.getContactInfoList() != null) {
            resumeDto.getContactInfoList().forEach(contactInfoDto -> {
                if (!resumeDao.existsTypeById(contactInfoDto.getTypeId())) {
                    log.warn("Contact type ID={} not found", contactInfoDto.getTypeId());
                    throw new ResourceNotFoundException("Contact type ID not found in database");
                }
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setResumeId(resumeId);
                contactInfo.setTypeId(contactInfoDto.getTypeId());
                contactInfo.setValue(contactInfoDto.getValue());
                resumeDao.createContactInfo(contactInfo);
            });
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
                resumeDao.createEducationInfo(educationInfo);
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
                resumeDao.createWorkExperienceInfo(workExperienceInfo);
            });
        }

        log.debug("Resume details saved for resumeId={}", resumeId);
    }

    private ResumeDto convertToDto(Resume resume) {
        return ResumeDto.builder()
                .name(resume.getName())
                .categoryId(resume.getCategoryId())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .contactInfoList(resumeDao.findContactInfoByResumeId(resume.getId()).stream()
                        .map(ci -> ContactInfoDto.builder()
                                .typeId(ci.getTypeId())
                                .value(ci.getValue())
                                .build())
                        .collect(Collectors.toList()))
                .educationInfoList(resumeDao.findEducationInfoByResumeId(resume.getId()).stream()
                        .map(ei -> EducationInfoDto.builder()
                                .institution(ei.getInstitution())
                                .program(ei.getProgram())
                                .startDate(ei.getStartDate())
                                .endDate(ei.getEndDate())
                                .degree(ei.getDegree())
                                .build())
                        .collect(Collectors.toList()))
                .workExperienceInfoList(resumeDao.findWorkExperienceInfoByResumeId(resume.getId()).stream()
                        .map(wei -> WorkExperienceInfoDto.builder()
                                .companyName(wei.getCompanyName())
                                .position(wei.getPosition())
                                .years(wei.getYears())
                                .responsibilities(wei.getResponsibilities())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
