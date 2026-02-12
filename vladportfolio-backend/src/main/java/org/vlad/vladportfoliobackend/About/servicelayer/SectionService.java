package org.vlad.vladportfoliobackend.About.servicelayer;

import org.vlad.vladportfoliobackend.About.datalayer.SectionRequestDTO;
import org.vlad.vladportfoliobackend.About.datalayer.SectionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SectionService {

    Mono<SectionResponseDTO> getSectionByTitle(String title);

    Mono<SectionResponseDTO> changeSection(Long id, SectionRequestDTO updated);

    Flux<SectionResponseDTO> getAllSections();
}
