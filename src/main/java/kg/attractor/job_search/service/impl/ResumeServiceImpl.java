package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
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
    public boolean createResume(ResumeDto resumeDto, Integer userId) {
        if (resumeDto == null || userId == null || userId <= 0) {
            return false;
        }

        if (!resumeDao.existsApplicantById(userId)) {
            return false;
        }

        if (!resumeDao.existsCategoryById(resumeDto.getCategoryId())) {
            return false;
        }

        Resume resume = new Resume();
        resume.setApplicantId(userId);
        resume.setName(resumeDto.getName());
        resume.setCategoryId(resumeDto.getCategoryId());
        resume.setSalary(resumeDto.getSalary());
        resume.setIsActive(resumeDto.getIsActive());
        resume.setCreated_date(LocalDateTime.now());
        resume.setUpdate_time(LocalDateTime.now());

        int rowsAffected = resumeDao.createResume(resume);

        if (rowsAffected != 1) {
            return false;
        }

        saveResumeDetails(resume.getId(), resumeDto);
        return true;
    }


    @Override
    @Transactional
    public boolean deleteResume(Integer resumeId) {
        if (resumeDao.findById(resumeId).isEmpty()) {
            return false;
        }

        resumeDao.deleteContactInfoByResumeId(resumeId);
        resumeDao.deleteEducationInfoByResumeId(resumeId);
        resumeDao.deleteWorkExperienceInfoByResumeId(resumeId);
        return resumeDao.deleteResume(resumeId);
    }

    @Override
    @Transactional
    public ResumeDto editResume(Integer resumeId, ResumeDto resumeDto) {
        Optional<Resume> optionalResume = resumeDao.findById(resumeId);


        if (optionalResume.isEmpty()) {
            return null;
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
        }

        return resumeDto;
    }

    @Override
    public List<ResumeDto> getAllResumes() {
        return resumeDao.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<List<ResumeDto>> getResumesByUserId(Integer userId) {
        return resumeDao.findByUserId(userId).map(resumes -> resumes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    private void saveResumeDetails(Integer resumeId, ResumeDto resumeDto) {
        if (resumeDto.getContactInfoList() != null) {
            resumeDto.getContactInfoList().forEach(contactInfoDto -> {
                if (!resumeDao.existsTypeById(contactInfoDto.getTypeId())) {
                    throw new IllegalArgumentException("Contact type ID not found in database");
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
