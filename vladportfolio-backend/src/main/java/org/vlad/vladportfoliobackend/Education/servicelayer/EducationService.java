package org.vlad.vladportfoliobackend.Education.servicelayer;

import org.vlad.vladportfoliobackend.Education.datalayer.EducationRequestDTO;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationResponseDTO;

import java.util.List;

public interface EducationService {
    List<EducationResponseDTO> getAllEducation();
    void deleteEducationById(Long id);
    EducationResponseDTO addEducation(EducationRequestDTO education);
}
