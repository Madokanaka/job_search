package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ResumeDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ResumeService {
    void createResume(ResumeDto resumeDto, Integer userId);

    void editResume(Integer resumeId, ResumeDto resumeDto);

    void deleteResume(Integer resumeId);

    ResumeDto getResumeById(Integer resumeId);

    List<ResumeDto> getAllResumes();

    Optional<List<ResumeDto>> getResumesByUserId(Integer userId);

    Map<Integer, String> getCategories();
}
