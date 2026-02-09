package org.vlad.vladportfoliobackend.Hobbies.datalayer;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hobbies")
public class Hobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hobbyName;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    private List<String> tags;
}
