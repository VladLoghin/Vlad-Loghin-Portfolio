package org.vlad.vladportfoliobackend.About.datalayer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("sections")
public class Section {
    @Id
    private Long id;
    private String title;
    private String type;
    private String body;
    private String items;
}
