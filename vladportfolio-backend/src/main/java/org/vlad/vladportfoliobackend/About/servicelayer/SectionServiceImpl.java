package org.vlad.vladportfoliobackend.About.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.About.datalayer.Section;
import org.vlad.vladportfoliobackend.About.datalayer.SectionRequestDTO;
import org.vlad.vladportfoliobackend.About.datalayer.SectionResponseDTO;
import org.vlad.vladportfoliobackend.About.datalayer.SectionType;
import org.vlad.vladportfoliobackend.About.repositorylayer.SectionRepository;
import org.vlad.vladportfoliobackend.utils.JsonUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Mono<SectionResponseDTO> getSectionByTitle(String title) {
        return sectionRepository.findByTitle(title)
                .switchIfEmpty(Mono.error(new RuntimeException("Section not found: " + title)))
                .map(SectionResponseDTO::from);
    }

    @Override
    public Mono<SectionResponseDTO> changeSection(Long id, SectionRequestDTO dto) {
        return sectionRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Section not found: " + id)))
                .flatMap(section -> {
                    section.setTitle(dto.getTitle());
                    section.setType(dto.getType().name());

                    // enforce consistency based on type
                    if (dto.getType() == SectionType.PARAGRAPH) {
                        section.setBody(dto.getBody());
                        section.setItems(JsonUtils.toJson(null));
                    } else {
                        section.setBody(null);
                        section.setItems(JsonUtils.toJson(dto.getItems()));
                    }

                    return sectionRepository.save(section);
                })
                .map(SectionResponseDTO::from);
    }

    @Override
    public Flux<SectionResponseDTO> getAllSections() {
        return sectionRepository.findAll()
                .map(SectionResponseDTO::from);
    }
}
