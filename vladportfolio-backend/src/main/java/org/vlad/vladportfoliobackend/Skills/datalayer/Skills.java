package org.vlad.vladportfoliobackend.Skills.datalayer;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "skill_name")
    private String skillName;

    @ElementCollection
    @CollectionTable(
            name = "skill_subskills",
            joinColumns = @JoinColumn(name = "skill_id")
    )
    @Column(name = "subskill")
    @OrderColumn(name = "subskill_order")
    private List<String> subskills = new ArrayList<>();
}
