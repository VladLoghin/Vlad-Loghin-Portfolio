package org.vlad.vladportfoliobackend.Skills.datalayer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("skills")
public class Skills {
    @Id
    private Integer id;
    private String skillName;
    private String subskills;
    private boolean active = true;
}
