package org.vlad.vladportfoliobackend.Education.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationRequestDTO;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationResponseDTO;
import org.vlad.vladportfoliobackend.Education.servicelayer.EducationService;

import java.util.List;

@RestController
@RequestMapping("api/education")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping()
    public List<EducationResponseDTO> getAllEducation() {
        return educationService.getAllEducation();
    }

    @DeleteMapping("/{id}")
    public void deleteEducationById(@PathVariable Long id) {
        educationService.deleteEducationById(id);
    }

    @PostMapping()
    public EducationResponseDTO addEducation(@RequestBody EducationRequestDTO educationRequestDTO) {
        return educationService.addEducation(educationRequestDTO);
    }
}