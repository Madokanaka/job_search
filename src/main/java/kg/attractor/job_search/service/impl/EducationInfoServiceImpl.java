package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.EducationInfoDao;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.service.EducationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationInfoServiceImpl implements EducationInfoService {

    private final EducationInfoDao educationInfoDao;

    @Override
    @Transactional
    public void createEducationInfo(List<EducationInfoDto> educationInfoDtoList, Integer resumeId) {
        if (educationInfoDtoList != null) {
            if (!educationInfoDtoList.isEmpty()) {
            educationInfoDtoList.forEach(educationInfoDto -> {
                EducationInfo educationInfo = new EducationInfo();
                educationInfo.setResumeId(resumeId);
                educationInfo.setInstitution(educationInfoDto.getInstitution());
                educationInfo.setDegree(educationInfoDto.getDegree());
                educationInfo.setProgram(educationInfoDto.getProgram());
                educationInfo.setEndDate(educationInfoDto.getEndDate());
                educationInfo.setStartDate(educationInfoDto.getStartDate());
                educationInfoDao.createEducationInfo(educationInfo);
            });
            }
        }
    }

    @Override
    public void deleteEducationInfoByResumeId(Integer resumeId) {
        if (educationInfoDao.findById(resumeId).isEmpty()) {
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }
        educationInfoDao.deleteEducationInfoByResumeId(resumeId);
    }

    @Override
    public List<EducationInfoDto> getEducationInfoByResumeId(Integer resumeId) {
        if (educationInfoDao.findById(resumeId).isEmpty()) {
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }
        List<EducationInfo> educationInfoList = educationInfoDao.findEducationInfoByResumeId(resumeId);

        return educationInfoList.stream()
                .map(educationInfo -> new EducationInfoDto(
                        educationInfo.getInstitution(),
                        educationInfo.getProgram(),
                        educationInfo.getStartDate(),
                        educationInfo.getEndDate(),
                        educationInfo.getDegree()))
                .collect(Collectors.toList());
    }
}
