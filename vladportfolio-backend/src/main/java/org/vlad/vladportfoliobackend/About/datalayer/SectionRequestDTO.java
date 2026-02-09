package org.vlad.vladportfoliobackend.About.datalayer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SectionRequestDTO {
    private String title;
    private SectionType type;     // required
    private String body;          // used when type == PARAGRAPH
    private List<String> items;   // used when type == BULLETS or TAGS
}

