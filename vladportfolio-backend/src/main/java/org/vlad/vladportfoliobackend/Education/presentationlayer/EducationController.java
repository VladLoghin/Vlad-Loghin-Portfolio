package org.vlad.vladportfoliobackend.Education.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationRequestDTO;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationResponseDTO;
import org.vlad.vladportfoliobackend.Education.servicelayer.EducationService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping()
    public Flux<EducationResponseDTO> getAllEducation() {
        return educationService.getAllEducation();
    }

    @PostMapping()
    public Mono<EducationResponseDTO> addEducation(@RequestBody EducationRequestDTO educationRequestDTO) {
        return educationService.addEducation(educationRequestDTO);
    }

    @PutMapping("/{id}")
    public Mono<EducationResponseDTO> updateEducation(@PathVariable Long id, @RequestBody EducationRequestDTO dto) {
        return educationService.updateEducation(id, dto);
    }

    @PatchMapping("/{id}/active")
    public Mono<Void> toggleActive(@PathVariable Long id, @RequestParam boolean active) {
        return educationService.toggleActive(id, active);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteEducationById(@PathVariable Long id) {
        return educationService.deleteEducationById(id);
    }
}
