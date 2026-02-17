package org.vlad.vladportfoliobackend.Reviews.datalayer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("reviews")
public class Review {
    @Id
    private Long id;
    private String reviewerName;
    private String content;
    private String rating;
    private boolean approved = false;
    private Integer displayOrder = 0;
}
