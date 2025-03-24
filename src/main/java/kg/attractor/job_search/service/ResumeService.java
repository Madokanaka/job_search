package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ResumeDto;

import java.util.List;
import java.util.Optional;

public interface ResumeService {
    boolean createResume(ResumeDto resumeDto, Integer userId);

    ResumeDto editResume(Integer resumeId, ResumeDto resumeDto);

    boolean deleteResume(Integer resumeId);

    List<ResumeDto> getAllResumes();

    Optional<List<ResumeDto>> getResumesByUserId(Integer userId);
}
