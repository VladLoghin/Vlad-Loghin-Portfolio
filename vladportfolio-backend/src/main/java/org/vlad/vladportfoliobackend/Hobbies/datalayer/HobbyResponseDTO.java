package org.vlad.vladportfoliobackend.Hobbies.datalayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vlad.vladportfoliobackend.utils.JsonUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HobbyResponseDTO {
    private String id;
    private String title;
    private String description;
    private List<String> tags;
    private boolean active;
    private int displayOrder;

    public static HobbyResponseDTO from(Hobby hobby) {
        return new HobbyResponseDTO(
                String.valueOf(hobby.getId()),
                hobby.getHobbyName(),
                hobby.getDescription(),
                JsonUtils.fromJson(hobby.getTags()),
                hobby.isActive(),
                hobby.getDisplayOrder()
        );
    }
}
