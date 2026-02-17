package org.vlad.vladportfoliobackend.Education.presentationlayer;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationRequestDTO;
import org.vlad.vladportfoliobackend.Education.datalayer.EducationResponseDTO;
import org.vlad.vladportfoliobackend.Education.servicelayer.EducationService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @PreAuthorize("hasRole('Admin')")
    @PostMapping()
    public Mono<EducationResponseDTO> addEducation(@RequestBody EducationRequestDTO educationRequestDTO) {
        return educationService.addEducation(educationRequestDTO);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{id}")
    public Mono<EducationResponseDTO> updateEducation(@PathVariable Long id, @RequestBody EducationRequestDTO dto) {
        return educationService.updateEducation(id, dto);
    }

    @PreAuthorize("hasRole('Admin')")
    @PatchMapping("/{id}/active")
    public Mono<Void> toggleActive(@PathVariable Long id, @RequestParam boolean active) {
        return educationService.toggleActive(id, active);
    }
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteEducationById(@PathVariable Long id) {
        return educationService.deleteEducationById(id);
    }

    @PutMapping("/reorder")
    public Mono<Void> reorder(@RequestBody List<Long> orderedIds) {
        return educationService.reorder(orderedIds);
    }
}
