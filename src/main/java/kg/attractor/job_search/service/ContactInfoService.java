package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ContactInfoDto;

import java.util.List;

public interface ContactInfoService {
    void createContactInfo(List<ContactInfoDto> contactInfoDtoList, Integer resumeId);

    void deleteContactInfoByResumeId(Integer resumeId);

    List<ContactInfoDto> getContactInfoByResumeId(Integer resumeId);
}
