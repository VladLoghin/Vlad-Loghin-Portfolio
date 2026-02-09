package org.vlad.vladportfoliobackend.Projects.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Projects.datalayer.Project;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectRequestDTO;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectResponseDTO;
import org.vlad.vladportfoliobackend.Projects.repositorylayer.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO request) {
        Project entity = mapToEntity(request);
        Project saved = projectRepository.save(entity);
        return mapToResponseDTO(saved);
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

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
            existing.setSkills(request.getSkills());
        }

        Project saved = projectRepository.save(existing);
        return mapToResponseDTO(saved);
    }

    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    // --------- MAPPERS ---------

    private Project mapToEntity(ProjectRequestDTO request) {
        Project p = new Project();
        p.setProjectName(request.getProjectName());
        p.setTag(request.getTag());
        p.setDescription(request.getDescription());
        p.setSkills(request.getSkills() != null ? request.getSkills() : new ArrayList<>());
        return p;
    }

    private ProjectResponseDTO mapToResponseDTO(Project entity) {
        return new ProjectResponseDTO(
                entity.getId(),
                entity.getProjectName(),
                entity.getTag(),
                entity.getDescription(),
                entity.getSkills() != null ? entity.getSkills() : new ArrayList<>()

        );
    }
}
