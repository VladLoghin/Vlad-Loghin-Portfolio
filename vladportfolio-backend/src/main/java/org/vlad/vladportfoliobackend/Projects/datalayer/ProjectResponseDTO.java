package org.vlad.vladportfoliobackend.Projects.datalayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vlad.vladportfoliobackend.utils.JsonUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDTO {
    private Long id;
    private String name;
    private String badge;
    private String description;
    private List<String> tags;
    private String githubUrl;
    private boolean active;

    public static ProjectResponseDTO from(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getProjectName(),
                project.getTag(),
                project.getDescription(),
                JsonUtils.fromJson(project.getSkills()),
                project.getGithubUrl(),
                project.isActive()
        );
    }
}
