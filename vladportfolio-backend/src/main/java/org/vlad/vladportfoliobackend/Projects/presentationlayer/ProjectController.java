package org.vlad.vladportfoliobackend.Projects.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Projects.datalayer.Project;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectRequestDTO;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectResponseDTO;
import org.vlad.vladportfoliobackend.Projects.servicelayer.ProjectService;

import java.util.List;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectResponseDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    public ProjectResponseDTO createProject(@RequestBody ProjectRequestDTO project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    public ProjectResponseDTO updateProject(@PathVariable Long id, @RequestBody ProjectRequestDTO project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

}
