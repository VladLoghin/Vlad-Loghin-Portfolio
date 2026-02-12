package org.vlad.vladportfoliobackend.Skills.datalayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vlad.vladportfoliobackend.utils.JsonUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillsResponseDTO {
    private String id;
    private String category;
    private List<String> items;
    private boolean active;

    public static SkillsResponseDTO from(Skills skill) {
        return new SkillsResponseDTO(
                String.valueOf(skill.getId()),
                skill.getSkillName(),
                JsonUtils.fromJson(skill.getSubskills()),
                skill.isActive()
        );
    }
}
