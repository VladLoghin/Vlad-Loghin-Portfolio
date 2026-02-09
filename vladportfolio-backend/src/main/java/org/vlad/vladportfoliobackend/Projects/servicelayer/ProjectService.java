package org.vlad.vladportfoliobackend.Projects.servicelayer;

import org.vlad.vladportfoliobackend.Projects.datalayer.Project;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectRequestDTO;
import org.vlad.vladportfoliobackend.Projects.datalayer.ProjectResponseDTO;

import java.util.List;

public interface ProjectService {
    public List<ProjectResponseDTO> getAllProjects();
    public ProjectResponseDTO createProject(ProjectRequestDTO project);
    public void deleteProject(Long id);

    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO project);
}
