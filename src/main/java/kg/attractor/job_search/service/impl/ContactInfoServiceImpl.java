package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ContactInfoDao;
import kg.attractor.job_search.dto.ContactInfoDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.service.ContactInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoDao contactInfoDao;

    @Override
    @Transactional
    public void createContactInfo(List<ContactInfoDto> contactInfoDtoList, Integer resumeId) {
        if (contactInfoDtoList != null || !contactInfoDtoList.isEmpty()) {
            contactInfoDtoList.forEach(contactInfoDto -> {
                if (!contactInfoDao.existsTypeById(contactInfoDto.getTypeId())) {
                    throw new ResourceNotFoundException("Contact type ID not found in database");
                }
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setResumeId(resumeId);
                contactInfo.setTypeId(contactInfoDto.getTypeId());
                contactInfo.setValue(contactInfoDto.getValue());
                contactInfoDao.createContactInfo(contactInfo);
            });
        }
    }

    @Override
    public void deleteContactInfoByResumeId(Integer resumeId) {
        contactInfoDao.deleteContactInfoByResumeId(resumeId);
    }

    @Override
    public List<ContactInfoDto> getContactInfoByResumeId(Integer resumeId) {
        List<ContactInfo> contactInfoList = contactInfoDao.findContactInfoByResumeId(resumeId);

        return contactInfoList.stream()
                .map(contactInfo -> new ContactInfoDto(
                        contactInfo.getTypeId(),
                        contactInfo.getValue()))
                .collect(Collectors.toList());
    }
}
