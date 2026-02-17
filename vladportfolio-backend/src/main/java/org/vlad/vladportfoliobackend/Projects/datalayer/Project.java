package org.vlad.vladportfoliobackend.Projects.datalayer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("projects")
public class Project {
    @Id
    private Long id;
    private String projectName;
    private String tag;
    private String description;
    private String skills;
    private String githubUrl;
    private boolean active = true;
    private Integer displayOrder = 0;
}
