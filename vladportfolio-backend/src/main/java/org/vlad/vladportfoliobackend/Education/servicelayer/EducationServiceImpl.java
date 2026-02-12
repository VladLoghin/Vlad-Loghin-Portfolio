package org.vlad.vladportfoliobackend.Education.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Education.datalayer.Education;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationRequestDTO;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationResponseDTO;
import org.vlad.vladportfoliobackend.Education.repositorylayer.EducationRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;

    public EducationServiceImpl(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    @Override
    public Flux<EducationResponseDTO> getAllEducation() {
        return educationRepository.findAll()
                .map(EducationResponseDTO::from);
    }

    @Override
    public Mono<Void> deleteEducationById(Long id) {
        return educationRepository.deleteById(id);
    }

    @Override
    public Mono<EducationResponseDTO> addEducation(EducationRequestDTO education) {
        Education newEducation = new Education();
        newEducation.setInstitutionName(education.getInstitutionName());
        newEducation.setDegree(education.getDegree());
        return educationRepository.save(newEducation)
                .map(EducationResponseDTO::from);
    }

    @Override
    public Mono<EducationResponseDTO> updateEducation(Long id, EducationRequestDTO dto) {
        return educationRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Education not found: " + id)))
                .flatMap(existing -> {
                    existing.setInstitutionName(dto.getInstitutionName());
                    existing.setDegree(dto.getDegree());
                    return educationRepository.save(existing);
                })
                .map(EducationResponseDTO::from);
    }

    @Override
    public Mono<Void> toggleActive(Long id, boolean active) {
        return educationRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Education not found: " + id)))
                .flatMap(existing -> {
                    existing.setActive(active);
                    return educationRepository.save(existing);
                })
                .then();
    }
}
