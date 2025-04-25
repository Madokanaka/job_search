package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ResumeDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.ContactType;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.repository.ContactInfoRepository;
import kg.attractor.job_search.service.ContactInfoService;
import kg.attractor.job_search.service.ContactTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;
    private final ContactTypeService contactTypeService;

    @Transactional
    @Override
    public void createContactInfo(ResumeDto resumeDto, Resume resume) {
        log.info("Creating contact info for resumeId={}", resume.getId());

        if (resumeDto.getContactEmail() != null && !resumeDto.getContactEmail().isBlank()) {
            saveContactInfo(resume, "Email", resumeDto.getContactEmail());
        }
        if (resumeDto.getPhoneNumber() != null && !resumeDto.getPhoneNumber().isBlank()) {
            saveContactInfo(resume, "Телефон", resumeDto.getPhoneNumber());
        }
        if (resumeDto.getLinkedIn() != null && !resumeDto.getLinkedIn().isBlank()) {
            saveContactInfo(resume, "LinkedIn", resumeDto.getLinkedIn());
        }
        if (resumeDto.getTelegram() != null && !resumeDto.getTelegram().isBlank()) {
            saveContactInfo(resume, "Telegram", resumeDto.getTelegram());
        }
        if (resumeDto.getFacebook() != null && !resumeDto.getFacebook().isBlank()) {
            saveContactInfo(resume, "Facebook", resumeDto.getFacebook());
        }
    }

    private void saveContactInfo(Resume resume, String type, String value) {
        log.debug("Saving contact info for type={} and value={}", type, value);

        ContactType contactType = contactTypeService.findByType(type)
                .orElseThrow(() -> {
                    log.warn("Contact type {} not found", type);
                    return new ResourceNotFoundException("Contact type " + type + " not found in database");
                });

        ContactInfo contactInfo = ContactInfo.builder()
                .resume(resume)
                .type(contactType)
                .infoValue(value)
                .build();

        contactInfoRepository.save(contactInfo);
        log.debug("Contact info saved: {}", contactInfo);
    }


    @Override
    @Transactional
    public void deleteContactInfoByResumeId(Integer resumeId) {
        log.info("Deleting contact info for resumeId={}", resumeId);
        if (contactInfoRepository.findByResumeId(resumeId).isEmpty()) {
            log.warn("Resume not found with id={}", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }
        contactInfoRepository.deleteByResumeId(resumeId);
        log.info("Contact info deleted for resumeId={}", resumeId);
    }

    @Override
    public List<ContactInfo> getContactInfoByResumeId(Integer resumeId) {
        log.info("Fetching contact info for resumeId={}", resumeId);
        List<ContactInfo> contactInfoList = contactInfoRepository.findByResumeId(resumeId);
        if (contactInfoList.isEmpty()) {
            log.warn("Resume not found with id={}", resumeId);
            throw new ResumeNotFoundException("Resume with id " + resumeId + " not found");
        }

        return contactInfoList;
    }
}