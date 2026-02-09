package org.vlad.vladportfoliobackend.Skills.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillRequestDTO;
import org.vlad.vladportfoliobackend.Skills.datalayer.Skills;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillsResponseDTO;
import org.vlad.vladportfoliobackend.Skills.repositorylayer.SkillsRepository;

import java.util.List;

@Service
public class SkillsServiceImpl implements SkillsService {

    private final SkillsRepository skillsRepository;

    public SkillsServiceImpl(SkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    @Override
    public List<SkillsResponseDTO> getAllSkills() {
        return skillsRepository.findAll()
                .stream()
                .map(SkillsResponseDTO::from)
                .toList();
    }

    @Override
    public SkillsResponseDTO addSkill(SkillRequestDTO skill) {
        Skills newSkill = new Skills();
        newSkill.setSkillName(skill.getSkillName());
        newSkill.setSubskills(skill.getSubskills());
        Skills saved = skillsRepository.save(newSkill);
        return SkillsResponseDTO.from(saved);
    }

    @Override
    public void deleteSkill(int skillId) {
        skillsRepository.deleteById(skillId);
    }
}
