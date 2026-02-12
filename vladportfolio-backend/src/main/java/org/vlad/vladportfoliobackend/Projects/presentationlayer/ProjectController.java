package org.vlad.vladportfoliobackend.Projects.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectRequestDTO;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectResponseDTO;
import org.vlad.vladportfoliobackend.Projects.servicelayer.ProjectService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @PostMapping
    public Mono<ProjectResponseDTO> createProject(@RequestBody ProjectRequestDTO project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    public Mono<ProjectResponseDTO> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDTO project) {
        return projectService.updateProject(id, project);
    }

    @PatchMapping("/{id}/active")
    public Mono<Void> toggleActive(@PathVariable Long id, @RequestParam boolean active) {
        return projectService.toggleActive(id, active);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProject(@PathVariable Long id) {
        return projectService.deleteProject(id);
    }
}
