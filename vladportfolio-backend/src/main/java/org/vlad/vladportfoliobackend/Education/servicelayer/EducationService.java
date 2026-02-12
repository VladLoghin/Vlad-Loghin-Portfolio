package org.vlad.vladportfoliobackend.Education.servicelayer;

import org.vlad.vladportfoliobackend.Education.datalayer.EducationRequestDTO;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EducationService {
    Flux<EducationResponseDTO> getAllEducation();
    Mono<Void> deleteEducationById(Long id);
    Mono<EducationResponseDTO> addEducation(EducationRequestDTO education);
    Mono<EducationResponseDTO> updateEducation(Long id, EducationRequestDTO education);
    Mono<Void> toggleActive(Long id, boolean active);
}
