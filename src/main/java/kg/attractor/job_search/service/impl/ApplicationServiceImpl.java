package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ApplicationDao;
import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationDao applicationDao;

    @Override
    public RespondenApplicantDto respondToVacancy(Integer resumeId, Integer vacancyId) {
        return applicationDao.saveResponse(resumeId, vacancyId);
    }
}
