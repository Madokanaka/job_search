package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.WorkExperienceInfoDao;
import kg.attractor.job_search.dto.WorkExperienceInfoDto;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkExperienceInfoServiceImpl implements WorkExperienceInfoService {

    private final WorkExperienceInfoDao workExperienceInfoDao;

    @Transactional
    @Override
    public void createWorkExperienceInfo(List<WorkExperienceInfoDto> workExperienceInfoDtoList, Integer resumeId) {
        if (workExperienceInfoDtoList != null) {
            if (!workExperienceInfoDtoList.isEmpty()) {
            workExperienceInfoDtoList.forEach(workExperienceInfoDto -> {
                WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
                workExperienceInfo.setResumeId(resumeId);
                workExperienceInfo.setYears(workExperienceInfoDto.getYears());
                workExperienceInfo.setCompanyName(workExperienceInfoDto.getCompanyName());
                workExperienceInfo.setPosition(workExperienceInfoDto.getPosition());
                workExperienceInfo.setResponsibilities(workExperienceInfoDto.getResponsibilities());
                workExperienceInfoDao.createWorkExperienceInfo(workExperienceInfo);
            });
            }
        }
    }

    @Override
    public void deleteWorkExperienceInfoByResumeId(Integer resumeId) {
        if (workExperienceInfoDao.findById(resumeId).isEmpty()) {
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }
        workExperienceInfoDao.deleteWorkExperienceInfoByResumeId(resumeId);
    }

    @Override
    public List<WorkExperienceInfoDto> getWorkExperienceInfoByResumeId(Integer resumeId) {
        if (workExperienceInfoDao.findById(resumeId).isEmpty()) {
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }
        List<WorkExperienceInfo> workExperienceInfoList = workExperienceInfoDao.findWorkExperienceInfoByResumeId(resumeId);

        return workExperienceInfoList.stream()
                .map(workExperienceInfo -> new WorkExperienceInfoDto(
                        workExperienceInfo.getYears(),
                        workExperienceInfo.getCompanyName(),
                        workExperienceInfo.getPosition(),
                        workExperienceInfo.getResponsibilities()))
                .collect(Collectors.toList());
    }
}
