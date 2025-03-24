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
    public void createResume(ResumeDto resumeDto, Integer userId) {
        Resume resume = new Resume();
        resume.setApplicantId(userId);

        if (!resumeDto.getName().isEmpty())
            resume.setName(resumeDto.getName());
        if (resumeDto.getCategoryId() != null && resumeDto.getCategoryId() != 0) {
            resume.setCategoryId(resumeDto.getCategoryId());
        }
        if (resumeDto.getSalary() != null && resumeDto.getSalary() != 0) {
            resume.setSalary(resumeDto.getSalary());
        }

        resume.setIsActive(resumeDto.getIsActive());
        resume.setCreated_date(LocalDateTime.now());
        resume.setUpdate_time(LocalDateTime.now());

        resumeDao.createResume(resume);

        if (resumeDto.getContactInfoList() != null) {
            resumeDto.getContactInfoList().forEach(contactInfoDto -> {
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setResumeId(resume.getId());
                contactInfo.setTypeId(contactInfoDto.getTypeId());
                contactInfo.setValue(contactInfoDto.getValue());
                resumeDao.createContactInfo(contactInfo);
            });
        }

        if (resumeDto.getEducationInfoList() != null) {
            resumeDto.getEducationInfoList().forEach(educationInfoDto -> {
                EducationInfo educationInfo = new EducationInfo();
                educationInfo.setResumeId(resume.getId());
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
                workExperienceInfo.setResumeId(resume.getId());
                workExperienceInfo.setYears(workExperienceInfoDto.getYears());
                workExperienceInfo.setCompanyName(workExperienceInfoDto.getCompanyName());
                workExperienceInfo.setPosition(workExperienceInfoDto.getPosition());
                workExperienceInfo.setResponsibilities(workExperienceInfoDto.getResponsibilities());
                resumeDao.createWorkExperienceInfo(workExperienceInfo);
            });
        }
    }



    @Override
    @Transactional
    public boolean deleteResume(Integer resumeId) {
        Optional<Resume> optionalResume = resumeDao.findById(resumeId);

        if (optionalResume.isPresent()) {
            resumeDao.deleteContactInfoByResumeId(resumeId);
            resumeDao.deleteEducationInfoByResumeId(resumeId);
            resumeDao.deleteWorkExperienceInfoByResumeId(resumeId);

            return resumeDao.deleteResume(resumeId);
        }
        return false;
    }

    @Override
    @Transactional
    public ResumeDto editResume(Integer resumeId, ResumeDto resumeDto) {
        Optional<Resume> optionalResume = resumeDao.findById(resumeId);

        if (optionalResume.isPresent()) {
            Resume resume = optionalResume.get();

            if (!resumeDto.getName().isEmpty())
                resume.setName(resumeDto.getName());
            if (resumeDto.getCategoryId() != null && resumeDto.getCategoryId() != 0) {
                resume.setCategoryId(resumeDto.getCategoryId());
            }
            if (resumeDto.getSalary() != null && resumeDto.getSalary() != 0) {
                resume.setSalary(resumeDto.getSalary());
            }

            resume.setIsActive(resumeDto.getIsActive());
            resume.setUpdate_time(LocalDateTime.now());

            boolean isUpdated = resumeDao.updateResume(resume);

            if (isUpdated) {
                resumeDao.deleteContactInfoByResumeId(resume.getId());
                resumeDao.deleteEducationInfoByResumeId(resume.getId());
                resumeDao.deleteWorkExperienceInfoByResumeId(resume.getId());

                if (resumeDto.getContactInfoList() != null) {
                    resumeDto.getContactInfoList().forEach(contactInfoDto -> {
                        ContactInfo contactInfo = new ContactInfo();
                        contactInfo.setResumeId(resume.getId());
                        contactInfo.setTypeId(contactInfoDto.getTypeId());
                        contactInfo.setValue(contactInfoDto.getValue());
                        resumeDao.createContactInfo(contactInfo);
                    });
                }

                if (resumeDto.getEducationInfoList() != null) {
                    resumeDto.getEducationInfoList().forEach(educationInfoDto -> {
                        EducationInfo educationInfo = new EducationInfo();
                        educationInfo.setResumeId(resume.getId());
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
                        workExperienceInfo.setResumeId(resume.getId());
                        workExperienceInfo.setYears(workExperienceInfoDto.getYears());
                        workExperienceInfo.setCompanyName(workExperienceInfoDto.getCompanyName());
                        workExperienceInfo.setPosition(workExperienceInfoDto.getPosition());
                        workExperienceInfo.setResponsibilities(workExperienceInfoDto.getResponsibilities());
                        resumeDao.createWorkExperienceInfo(workExperienceInfo);
                    });
                }

                return ResumeDto.builder()
                        .name(resume.getName())
                        .categoryId(resume.getCategoryId())
                        .salary(resume.getSalary())
                        .isActive(resume.getIsActive())
                        .contactInfoList(resumeDto.getContactInfoList())
                        .educationInfoList(resumeDto.getEducationInfoList())
                        .workExperienceInfoList(resumeDto.getWorkExperienceInfoList())
                        .build();
            }
        }
        return null;
    }

    @Override
    public List<ResumeDto> getAllResumes() {
        List<Resume> resumes = resumeDao.findAll();
        return resumes.stream()
                .map(resume -> {
                    List<ContactInfo> contactInfos = resumeDao.findContactInfoByResumeId(resume.getId());
                    List<EducationInfo> educationInfos = resumeDao.findEducationInfoByResumeId(resume.getId());
                    List<WorkExperienceInfo> workExperienceInfos = resumeDao.findWorkExperienceInfoByResumeId(resume.getId());

                    return ResumeDto.builder()
                            .name(resume.getName())
                            .categoryId(resume.getCategoryId())
                            .salary(resume.getSalary())
                            .isActive(resume.getIsActive())
                            .contactInfoList(contactInfos.stream()
                                    .map(ci -> ContactInfoDto.builder()
                                            .typeId(ci.getTypeId())
                                            .value(ci.getValue())
                                            .build())
                                    .collect(Collectors.toList()))
                            .educationInfoList(educationInfos.stream()
                                    .map(ei -> EducationInfoDto.builder()
                                            .institution(ei.getInstitution())
                                            .program(ei.getProgram())
                                            .startDate(ei.getStartDate())
                                            .endDate(ei.getEndDate())
                                            .degree(ei.getDegree())
                                            .build())
                                    .collect(Collectors.toList()))
                            .workExperienceInfoList(workExperienceInfos.stream()
                                    .map(wei -> WorkExperienceInfoDto.builder()
                                            .companyName(wei.getCompanyName())
                                            .position(wei.getPosition())
                                            .years(wei.getYears())
                                            .responsibilities(wei.getResponsibilities())
                                            .build())
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<List<ResumeDto>> getResumesByUserId(Integer userId) {
        Optional<List<Resume>> optionalResumes = resumeDao.findByUserId(userId);
        if (optionalResumes.isPresent() && !optionalResumes.get().isEmpty()) {
            List<ResumeDto> resumeDtos = optionalResumes.get().stream()
                    .map(resume -> {
                        List<ContactInfo> contactInfos = resumeDao.findContactInfoByResumeId(resume.getId());
                        List<EducationInfo> educationInfos = resumeDao.findEducationInfoByResumeId(resume.getId());
                        List<WorkExperienceInfo> workExperienceInfos = resumeDao.findWorkExperienceInfoByResumeId(resume.getId());

                        return ResumeDto.builder()
                                .name(resume.getName())
                                .categoryId(resume.getCategoryId())
                                .salary(resume.getSalary())
                                .isActive(resume.getIsActive())
                                .contactInfoList(contactInfos.stream()
                                        .map(ci -> ContactInfoDto.builder()
                                                .typeId(ci.getTypeId())
                                                .value(ci.getValue())
                                                .build())
                                        .collect(Collectors.toList()))
                                .educationInfoList(educationInfos.stream()
                                        .map(ei -> EducationInfoDto.builder()
                                                .institution(ei.getInstitution())
                                                .program(ei.getProgram())
                                                .startDate(ei.getStartDate())
                                                .endDate(ei.getEndDate())
                                                .degree(ei.getDegree())
                                                .build())
                                        .collect(Collectors.toList()))
                                .workExperienceInfoList(workExperienceInfos.stream()
                                        .map(wei -> WorkExperienceInfoDto.builder()
                                                .companyName(wei.getCompanyName())
                                                .position(wei.getPosition())
                                                .years(wei.getYears())
                                                .responsibilities(wei.getResponsibilities())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build();
                    })
                    .collect(Collectors.toList());
            return Optional.of(resumeDtos);
        }
        return Optional.empty();
    }

}
