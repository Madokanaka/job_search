package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.EducationInfoDto;
import kg.attractor.job_search.model.Resume;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EducationInfoService {
    @Transactional
    void createEducationInfo(List<EducationInfoDto> educationInfoDtoList, Resume resume);

    void deleteEducationInfoByResumeId(Integer resumeId);

    List<EducationInfoDto> getEducationInfoByResumeId(Integer resumeId);
}
