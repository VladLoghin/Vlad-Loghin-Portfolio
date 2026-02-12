package org.vlad.vladportfoliobackend.Projects.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Projects.datalayer.Project;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectRequestDTO;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectResponseDTO;
import org.vlad.vladportfoliobackend.Projects.repositorylayer.ProjectRepository;
import org.vlad.vladportfoliobackend.utils.JsonUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Flux<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAllByOrderByIdAsc()
                .map(ProjectResponseDTO::from);
    }

    @Override
    public Mono<ProjectResponseDTO> createProject(ProjectRequestDTO request) {
        Project entity = new Project();
        entity.setProjectName(request.getProjectName());
        entity.setTag(request.getTag());
        entity.setDescription(request.getDescription());
        entity.setSkills(JsonUtils.toJson(request.getSkills()));
        entity.setGithubUrl(request.getGithubUrl());
        return projectRepository.save(entity)
                .map(ProjectResponseDTO::from);
    }

    @Override
    public Mono<ProjectResponseDTO> updateProject(Long id, ProjectRequestDTO request) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Project not found")))
                .flatMap(existing -> {
                    if (request.getProjectName() != null) {
                        existing.setProjectName(request.getProjectName());
                    }
                    if (request.getTag() != null) {
                        existing.setTag(request.getTag());
                    }
                    if (request.getDescription() != null) {
                        existing.setDescription(request.getDescription());
                    }
                    if (request.getSkills() != null) {
                        existing.setSkills(JsonUtils.toJson(request.getSkills()));
                    }
                    if (request.getGithubUrl() != null) {
                        existing.setGithubUrl(request.getGithubUrl());
                    }
                    return projectRepository.save(existing);
                })
                .map(ProjectResponseDTO::from);
    }

    @Override
    public Mono<Void> deleteProject(Long id) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Project not found")))
                .flatMap(existing -> projectRepository.deleteById(id));
    }

    @Override
    public Mono<Void> toggleActive(Long id, boolean active) {
        return projectRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Project not found")))
                .flatMap(existing -> {
                    existing.setActive(active);
                    return projectRepository.save(existing);
                })
                .then();
    }
}
