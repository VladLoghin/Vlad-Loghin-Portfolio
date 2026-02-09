package org.vlad.vladportfoliobackend.Skills.servicelayer;

import org.vlad.vladportfoliobackend.Skills.datalayer.SkillRequestDTO;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillsResponseDTO;

import java.util.List;

public interface SkillsService {

    List<SkillsResponseDTO> getAllSkills();

    SkillsResponseDTO addSkill(SkillRequestDTO skill);

    void deleteSkill(int skillId);
}
