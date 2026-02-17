package org.vlad.vladportfoliobackend.Projects.presentationlayer;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectRequestDTO;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectResponseDTO;
import org.vlad.vladportfoliobackend.Projects.servicelayer.ProjectService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public Flux<ProjectResponseDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping
    public Mono<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO project) {
        return projectService.createProject(project);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{id}")
    public Mono<ProjectResponseDTO> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDTO project) {
        return projectService.updateProject(id, project);
    }

    @PreAuthorize("hasRole('Admin')")
    @PatchMapping("/{id}/active")
    public Mono<Void> toggleActive(@PathVariable Long id, @RequestParam boolean active) {
        return projectService.toggleActive(id, active);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProject(@PathVariable Long id) {
        return projectService.deleteProject(id);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/reorder")
    public Mono<Void> reorder(@RequestBody List<Long> orderedIds) {
        return projectService.reorder(orderedIds);
    }
}
