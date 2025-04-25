package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.model.ContactType;
import kg.attractor.job_search.repository.ContactTypeRepository;
import kg.attractor.job_search.service.ContactTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactTypeServiceImpl implements ContactTypeService {
    private final ContactTypeRepository contactTypeRepository;

    @Override
    public boolean existsById(Integer id) {
        return contactTypeRepository.existsById(id);
    }

    @Override
    public Optional<ContactType> findByType(String type) {
        return contactTypeRepository.findByType(type);
    }

    @Override
    public Optional<ContactType> findById(Integer id) {
        return contactTypeRepository.findById(id);
    }
}