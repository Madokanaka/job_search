package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.model.Resume;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ResumeService {
    void createResume(ResumeDto resumeDto, Integer userId);

    void editResume(Integer resumeId, ResumeDto resumeDto);

    void deleteResume(Integer resumeId);

    ResumeDto getResumeById(Integer resumeId);

    Resume getResumeModelById(Integer resumeId);

    List<ResumeDto> getAllResumes();

    Optional<List<ResumeDto>> getResumesByUserId(Integer userId);

    Map<Integer, String> getCategories();

    Page<ResumeDto> getAllResumesPaged(String pageNumber, String pageSize);

    Page<ResumeDto> getResumesByUserIdPaged(Integer userId, String page, String size);

    int getCategoryIdByResumeId(Integer resumeId);

    List<ResumeDto> getResumesByUserAndCategory(org.springframework.security.core.userdetails.User principal, Integer categoryId);
}
