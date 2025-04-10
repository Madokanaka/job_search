package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.EducationInfoDao;
import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.service.EducationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationInfoServiceImpl implements EducationInfoService {

    private final EducationInfoDao educationInfoDao;

    @Override
    @Transactional
    public void createEducationInfo(List<EducationInfoDto> educationInfoDtoList, Integer resumeId) {
        log.info("Creating education info for resumeId={}", resumeId);

        if (educationInfoDtoList != null) {
            if (!educationInfoDtoList.isEmpty()) {
                educationInfoDtoList.forEach(educationInfoDto -> {
                    log.debug("Processing educationInfoDto: {}", educationInfoDto);
                    EducationInfo educationInfo = new EducationInfo();
                    educationInfo.setResumeId(resumeId);
                    educationInfo.setInstitution(educationInfoDto.getInstitution());
                    educationInfo.setDegree(educationInfoDto.getDegree());
                    educationInfo.setProgram(educationInfoDto.getProgram());
                    educationInfo.setEndDate(educationInfoDto.getEndDate());
                    educationInfo.setStartDate(educationInfoDto.getStartDate());
                    educationInfoDao.createEducationInfo(educationInfo);
                    log.debug("Education info saved: {}", educationInfo);
                });
            } else {
                log.info("Received empty educationInfoDto list for resumeId={}", resumeId);
            }
        } else {
            log.info("Received null educationInfoDto list for resumeId={}", resumeId);
        }
    }

    @Override
    public void deleteEducationInfoByResumeId(Integer resumeId) {
        log.info("Deleting education info for resumeId={}", resumeId);

        if (educationInfoDao.findById(resumeId).isEmpty()) {
            log.warn("Resume not found for deletion with id={}", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }

        educationInfoDao.deleteEducationInfoByResumeId(resumeId);
        log.info("Deleted education info for resumeId={}", resumeId);
    }

    @Override
    public List<EducationInfoDto> getEducationInfoByResumeId(Integer resumeId) {
        log.info("Fetching education info for resumeId={}", resumeId);

        if (educationInfoDao.findById(resumeId).isEmpty()) {
            log.warn("Resume not found with id={}", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }

        List<EducationInfo> educationInfoList = educationInfoDao.findEducationInfoByResumeId(resumeId);
        List<EducationInfoDto> result = educationInfoList.stream()
                .map(educationInfo -> new EducationInfoDto(
                        educationInfo.getInstitution(),
                        educationInfo.getProgram(),
                        educationInfo.getStartDate(),
                        educationInfo.getEndDate(),
                        educationInfo.getDegree()))
                .collect(Collectors.toList());

        log.debug("Fetched {} education records for resumeId={}", result.size(), resumeId);
        return result;
    }
}
