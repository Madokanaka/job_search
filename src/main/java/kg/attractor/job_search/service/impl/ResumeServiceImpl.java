package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.DatabaseOperationException;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.service.ResumeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeDao resumeDao;

    public ResumeServiceImpl(ResumeDao resumeDao) {
        this.resumeDao = resumeDao;
    }

    @Override
    @Transactional
    public void createResume(ResumeDto resumeDto, Integer userId) {
        if (resumeDto == null || userId == null || userId <= 0) {
            throw new BadRequestException("User ID or resume have invalid values");
        }

        if (!resumeDao.existsApplicantById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " not found in database");
        }

        if (!resumeDao.existsCategoryById(resumeDto.getCategoryId())) {
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
            throw new DatabaseOperationException("Could not create resume");
        }

        saveResumeDetails(resumeId, resumeDto);
    }


    @Override
    @Transactional
    public void deleteResume(Integer resumeId) {
        if (resumeDao.findById(resumeId).isEmpty()) {
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }

        resumeDao.deleteContactInfoByResumeId(resumeId);
        resumeDao.deleteEducationInfoByResumeId(resumeId);
        resumeDao.deleteWorkExperienceInfoByResumeId(resumeId);
        if (!resumeDao.deleteResume(resumeId)) {
            throw new DatabaseOperationException("Could not delete resume");
        }
    }

    @Override
    @Transactional
    public void editResume(Integer resumeId, ResumeDto resumeDto) {
        Optional<Resume> optionalResume = resumeDao.findById(resumeId);

        if (optionalResume.isEmpty()) {
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }

        Resume resume = optionalResume.get();
        resume.setName(resumeDto.getName());
        resume.setSalary(resumeDto.getSalary());
        resume.setIsActive(resumeDto.getIsActive());
        resume.setUpdate_time(LocalDateTime.now());

        if (resumeDao.updateResume(resume)) {
            resumeDao.deleteContactInfoByResumeId(resume.getId());
            resumeDao.deleteEducationInfoByResumeId(resume.getId());
            resumeDao.deleteWorkExperienceInfoByResumeId(resume.getId());

            saveResumeDetails(resume.getId(), resumeDto);
        } else {
            throw new DatabaseOperationException("Could not update resume");
        }

    }

    @Override
    public List<ResumeDto> getAllResumes() {
        return resumeDao.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<List<ResumeDto>> getResumesByUserId(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new BadRequestException("User ID has invalid value");
        }

        if (!resumeDao.existsApplicantById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " not found in database");
        }

        return resumeDao.findByUserId(userId).map(resumes -> resumes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    private void saveResumeDetails(Integer resumeId, ResumeDto resumeDto) {
        if (resumeDto.getContactInfoList() != null) {
            resumeDto.getContactInfoList().forEach(contactInfoDto -> {
                if (!resumeDao.existsTypeById(contactInfoDto.getTypeId())) {
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
