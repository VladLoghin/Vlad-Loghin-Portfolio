package org.vlad.vladportfoliobackend.About.presentationlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.About.datalayer.SectionRequestDTO;
import org.vlad.vladportfoliobackend.About.datalayer.SectionResponseDTO;
import org.vlad.vladportfoliobackend.About.servicelayer.SectionService;

import java.util.List;

@RestController
@RequestMapping("/about")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping("{title}")
    public SectionResponseDTO getSectionByTitle(@PathVariable String title) {
        return sectionService.getSectionByTitle(title);
    }

    @PutMapping("/{id}")
    public SectionResponseDTO changeSection(@PathVariable Long id, @RequestBody SectionRequestDTO updated) {
        return sectionService.changeSection(id, updated);
    }

    @GetMapping()
    public List<SectionResponseDTO> getAllSections() {
        return sectionService.getAllSections();
    }
}
