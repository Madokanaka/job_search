package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.model.RespondedApplicant;
import kg.attractor.job_search.service.ApplicationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Override
    public RespondedApplicant respondToVacancy(Integer vacancyId, Integer resumeId) {
        // TODO: Проверить, существует ли вакансия и резюме
        // TODO: Создать отклик на вакансию (связать резюме с вакансией)
        // TODO: Сохранить отклик в хранилище

        return new RespondedApplicant();
    }

    @Override
    public List<RespondedApplicant> getRespondedApplicants(Integer vacancyId) {
        // TODO: Получить список откликнувшихся соискателей на вакансию
        return List.of(new RespondedApplicant());
    }

}
