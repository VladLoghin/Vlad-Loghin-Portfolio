package org.vlad.vladportfoliobackend.Skills.datalayer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SkillRequestDTO {

    @NotBlank
    private String skillName;

    @NotEmpty
    private List<String> subskills;
}
