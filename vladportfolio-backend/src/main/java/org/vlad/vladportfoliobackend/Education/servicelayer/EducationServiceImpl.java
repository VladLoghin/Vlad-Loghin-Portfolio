package org.vlad.vladportfoliobackend.Education.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Education.datalayer.Education;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationRequestDTO;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationResponseDTO;
import org.vlad.vladportfoliobackend.Education.repositorylayer.EducationRepository;

import java.util.List;

@Service
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;

    public EducationServiceImpl(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }
    @Override
    public List<EducationResponseDTO> getAllEducation() {
        return educationRepository.findAll()
                .stream()
                .map(EducationResponseDTO::from)
                .toList();
    }

    @Override
    public void deleteEducationById(Long id) {
        educationRepository.deleteById(id);
    }

    @Override
    public EducationResponseDTO addEducation(EducationRequestDTO education) {
        Education newEducation = new Education();
        newEducation.setInstitutionName(education.getInstitutionName());
        newEducation.setDegree(education.getDegree());
        Education saved = educationRepository.save(newEducation);
        return EducationResponseDTO.from(saved);
    }
}
