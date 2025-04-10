package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ContactInfoDao;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.service.ContactInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoDao contactInfoDao;

    @Override
    @Transactional
    public void createContactInfo(List<ContactInfoDto> contactInfoDtoList, Integer resumeId) {
        log.info("Creating contact info for resumeId={}", resumeId);
        if (contactInfoDtoList != null) {
            if (!contactInfoDtoList.isEmpty()) {
                contactInfoDtoList.forEach(contactInfoDto -> {
                    log.debug("Processing contactInfoDto: {}", contactInfoDto);
                    if (!contactInfoDao.existsTypeById(contactInfoDto.getTypeId())) {
                        log.warn("Contact type ID {} not found", contactInfoDto.getTypeId());
                        throw new ResourceNotFoundException("Contact type ID not found in database");
                    }
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setResumeId(resumeId);
                    contactInfo.setTypeId(contactInfoDto.getTypeId());
                    contactInfo.setValue(contactInfoDto.getValue());
                    contactInfoDao.createContactInfo(contactInfo);
                    log.debug("Contact info saved: {}", contactInfo);
                });
            } else {
                log.info("Contact info list is empty for resumeId={}", resumeId);
            }
        } else {
            log.info("Contact info list is null for resumeId={}", resumeId);
        }
    }

    @Override
    public void deleteContactInfoByResumeId(Integer resumeId) {
        log.info("Deleting contact info for resumeId={}", resumeId);
        if (contactInfoDao.findById(resumeId).isEmpty()) {
            log.warn("Resume not found with id={}", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }
        contactInfoDao.deleteContactInfoByResumeId(resumeId);
        log.info("Contact info deleted for resumeId={}", resumeId);
    }

    @Override
    public List<ContactInfoDto> getContactInfoByResumeId(Integer resumeId) {
        log.info("Fetching contact info for resumeId={}", resumeId);
        if (contactInfoDao.findById(resumeId).isEmpty()) {
            log.warn("Resume not found with id={}", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }
        List<ContactInfo> contactInfoList = contactInfoDao.findContactInfoByResumeId(resumeId);
        List<ContactInfoDto> result = contactInfoList.stream()
                .map(contactInfo -> new ContactInfoDto(
                        contactInfo.getTypeId(),
                        contactInfo.getValue()))
                .collect(Collectors.toList());
        log.debug("Fetched {} contact info records for resumeId={}", result.size(), resumeId);
        return result;
    }
}
