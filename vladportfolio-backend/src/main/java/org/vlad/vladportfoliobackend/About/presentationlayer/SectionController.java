package org.vlad.vladportfoliobackend.About.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.About.datalayer.SectionRequestDTO;
import org.vlad.vladportfoliobackend.About.datalayer.SectionResponseDTO;
import org.vlad.vladportfoliobackend.About.servicelayer.SectionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/about")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping()
    public Flux<SectionResponseDTO> getAllSections() {
        return sectionService.getAllSections();
    }

    @GetMapping("/{title}")
    public Mono<SectionResponseDTO> getSectionByTitle(@PathVariable String title) {
        return sectionService.getSectionByTitle(title);
    }

    @PutMapping("/{id}")
    public Mono<SectionResponseDTO> changeSection(@PathVariable Long id, @RequestBody SectionRequestDTO updated) {
        return sectionService.changeSection(id, updated);
    }
}
