package org.vlad.vladportfoliobackend.Reviews.datalayer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String reviewerName;
    private String content;

    @jakarta.persistence.Enumerated(jakarta.persistence.EnumType.STRING)
    private Rating rating;

    private boolean approved = false;
}
