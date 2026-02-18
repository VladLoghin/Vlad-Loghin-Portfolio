package org.vlad.vladportfoliobackend.Education.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Education.datalayer.Education;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationRequestDTO;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationResponseDTO;
import org.vlad.vladportfoliobackend.Education.repositorylayer.EducationRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;

    public EducationServiceImpl(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    @Override
    public Flux<EducationResponseDTO> getAllEducation() {
        return educationRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .map(EducationResponseDTO::from);
    }

    @Override
    public Mono<Void> deleteEducationById(Long id) {
        return educationRepository.deleteById(id);
    }

    @Override
    public Mono<EducationResponseDTO> addEducation(EducationRequestDTO education) {
        return educationRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .map(Education::getDisplayOrder)
                .defaultIfEmpty(0)
                .reduce(Math::max)
                .flatMap(maxOrder -> {
                    Education newEducation = new Education();
                    newEducation.setInstitutionName(education.getInstitutionName());
                    newEducation.setDegree(education.getDegree());
                    newEducation.setYears(education.getYears());
                    newEducation.setDisplayOrder(maxOrder + 1);
                    return educationRepository.save(newEducation);
                })
                .map(EducationResponseDTO::from);
    }

    @Override
    public Mono<EducationResponseDTO> updateEducation(Long id, EducationRequestDTO dto) {
        return educationRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Education not found: " + id)))
                .flatMap(existing -> {
                    existing.setInstitutionName(dto.getInstitutionName());
                    existing.setDegree(dto.getDegree());
                    existing.setYears(dto.getYears());
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

    @Override
    public Mono<Void> reorder(List<Long> orderedIds) {
        return Flux.fromIterable(orderedIds)
                .index()
                .flatMap(tuple -> educationRepository.findById(tuple.getT2())
                        .flatMap(e -> {
                            e.setDisplayOrder(tuple.getT1().intValue());
                            return educationRepository.save(e);
                        }))
                .then();
    }
}
