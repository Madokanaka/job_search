package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ResumeDao;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.dto.ResumeDto;
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
                return new ResumeDto(resume.getId(), resume.getName(), resume.getCategoryId(),
                        resume.getSalary(), resume.getIsActive(), resume.getApplicantId());
            }
        }
        return null;
    }

    @Override
    public boolean deleteResume(Integer resumeId) {
        Optional<Resume> optionalResume = resumeDao.findById(resumeId);

        if (optionalResume.isPresent()) {
            return resumeDao.deleteResume(resumeId);
        }
        return false;
    }

    @Override
    public List<ResumeDto> getAllResumes() {
        List<Resume> resumes = resumeDao.findAll();
        return resumes.stream()
                .map(resume -> new ResumeDto(resume.getId(), resume.getName(), resume.getCategoryId(),
                        resume.getSalary(), resume.getIsActive(), resume.getApplicantId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<List<ResumeDto>> getResumesByUserId(Integer userId) {
        Optional<List<Resume>> optionalResumes = resumeDao.findByUserId(userId);
        if (optionalResumes.isPresent() && !optionalResumes.get().isEmpty()) {
            List<ResumeDto> resumeDtos = optionalResumes.get().stream()
                    .map(resume -> new ResumeDto(resume.getId(), resume.getName(), resume.getCategoryId(),
                            resume.getSalary(), resume.getIsActive(), resume.getApplicantId()))
                    .collect(Collectors.toList());
            return Optional.of(resumeDtos);
        }
        return Optional.empty();
    }

}
