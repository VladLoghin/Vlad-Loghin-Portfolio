package org.vlad.vladportfoliobackend.About.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.About.datalayer.Section;
import org.vlad.vladportfoliobackend.About.datalayer.SectionRequestDTO;
import org.vlad.vladportfoliobackend.About.datalayer.SectionResponseDTO;
import org.vlad.vladportfoliobackend.About.datalayer.SectionType;
import org.vlad.vladportfoliobackend.About.repositorylayer.SectionRepository;

import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public SectionResponseDTO getSectionByTitle(String title) {
        Section section = sectionRepository.findByTitle(title);
        return SectionResponseDTO.from(section);
    }

    @Override
    public SectionResponseDTO changeSection(Long id, SectionRequestDTO dto) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found: " + id));

        section.setTitle(dto.getTitle());
        section.setType(dto.getType());

        // enforce consistency based on type
        if (dto.getType() == SectionType.PARAGRAPH) {
            section.setBody(dto.getBody());
            section.getItems().clear();
        } else {
            section.setBody(null);
            section.getItems().clear();
            if (dto.getItems() != null) {
                section.getItems().addAll(dto.getItems());
            }
        }

        Section saved = sectionRepository.save(section);
        return SectionResponseDTO.from(saved);
    }

    @Override
    public List<SectionResponseDTO> getAllSections() {
        return sectionRepository.findAll()
                .stream()
                .map(SectionResponseDTO::from)
                .toList();
    }

}
