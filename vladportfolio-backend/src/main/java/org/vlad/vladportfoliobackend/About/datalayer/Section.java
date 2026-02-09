package org.vlad.vladportfoliobackend.About.datalayer;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private SectionType type;

    // For PARAGRAPH sections (like About)
    @Column(length = 2000)
    private String body;

    // For BULLETS or TAGS (What I do / What I care about)
    @ElementCollection
    @CollectionTable(name = "section_items", joinColumns = @JoinColumn(name = "section_id"))
    @Column(name = "item")
    @OrderColumn(name = "item_order") // keeps bullet order stable
    private List<String> items = new ArrayList<>();
}
