package org.vlad.vladportfoliobackend.Projects.servicelayer;

import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectRequestDTO;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProjectService {
    Flux<ProjectResponseDTO> getAllProjects();
    Mono<ProjectResponseDTO> createProject(ProjectRequestDTO project);
    Mono<Void> deleteProject(Long id);
    Mono<ProjectResponseDTO> updateProject(Long id, ProjectRequestDTO project);
    Mono<Void> toggleActive(Long id, boolean active);
    Mono<Void> reorder(List<Long> orderedIds);
}
