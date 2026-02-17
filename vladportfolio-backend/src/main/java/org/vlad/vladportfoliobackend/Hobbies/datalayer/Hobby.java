package org.vlad.vladportfoliobackend.Hobbies.datalayer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("hobbies")
public class Hobby {
    @Id
    private Long id;
    private String hobbyName;
    private String description;
    private String tags;
    private boolean active = true;
    private Integer displayOrder = 0;
}
