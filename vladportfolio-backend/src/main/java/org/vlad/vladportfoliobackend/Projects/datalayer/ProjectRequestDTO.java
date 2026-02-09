package org.vlad.vladportfoliobackend.Projects.datalayer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectRequestDTO {
    private String projectName;
    private String tag;
    private String description;
    private java.util.List<String> skills;
}
