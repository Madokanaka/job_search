package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.Resume;

import java.util.List;

public interface ContactInfoService {

    void createContactInfo(ResumeDto resumeDto, Resume resume);

    void deleteContactInfoByResumeId(Integer resumeId);

    List<ContactInfo> getContactInfoByResumeId(Integer resumeId);
}
