package org.vlad.vladportfoliobackend.About.datalayer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SectionResponseDTO {
    private String id;
    private String title;
    private SectionType type;
    private String body;
    private List<String> items;

    public static SectionResponseDTO from(Section section) {
        SectionResponseDTO dto = new SectionResponseDTO();
        dto.setId(String.valueOf(section.getId()));
        dto.setTitle(section.getTitle());
        dto.setType(section.getType());
        dto.setBody(section.getBody());
        dto.setItems(section.getItems());
        return dto;
    }
}
