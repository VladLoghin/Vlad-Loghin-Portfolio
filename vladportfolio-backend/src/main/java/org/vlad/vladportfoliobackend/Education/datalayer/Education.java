package org.vlad.vladportfoliobackend.Education.datalayer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("education")
public class Education {
    @Id
    private Long id;
    private String institutionName;
    private String degree;
    private String years;
    private boolean active = true;
    private Integer displayOrder = 0;
}
