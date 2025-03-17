package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.model.Resume;

import java.util.List;
import java.util.Optional;

public interface ResumeService {
    Resume createResume(ResumeDto resumeDto, Integer userId);

    ResumeDto editResume(Integer resumeId, ResumeDto resumeDto);

    void deleteResume(Integer resumeId);

    List<ResumeDto> getAllResumes();

    Optional<ResumeDto> getResumeById(Integer resumeId);
}
