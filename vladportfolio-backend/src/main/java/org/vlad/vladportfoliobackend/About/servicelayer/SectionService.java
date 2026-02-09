package org.vlad.vladportfoliobackend.About.servicelayer;


import org.vlad.vladportfoliobackend.About.datalayer.Section;
import org.vlad.vladportfoliobackend.About.datalayer.SectionRequestDTO;
import org.vlad.vladportfoliobackend.About.datalayer.SectionResponseDTO;

import java.util.List;

public interface SectionService {

    public SectionResponseDTO getSectionByTitle(String title);

    public SectionResponseDTO changeSection(Long id, SectionRequestDTO updated);

    public List<SectionResponseDTO> getAllSections();


}
