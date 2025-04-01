package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.EducationInfoDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EducationInfoService {
    @Transactional
    void createEducationInfo(List<EducationInfoDto> educationInfoDtoList, Integer resumeId);

    void deleteEducationInfoByResumeId(Integer resumeId);

    List<EducationInfoDto> getEducationInfoByResumeId(Integer resumeId);
}
