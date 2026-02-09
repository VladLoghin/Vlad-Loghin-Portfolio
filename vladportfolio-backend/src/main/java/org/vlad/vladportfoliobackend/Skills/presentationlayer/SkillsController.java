package org.vlad.vladportfoliobackend.Skills.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillRequestDTO;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillsResponseDTO;
import org.vlad.vladportfoliobackend.Skills.servicelayer.SkillsService;

import java.util.List;


@RestController
@RequestMapping("/api/skills")
public class SkillsController {

    private final SkillsService skillsService;

    public SkillsController(SkillsService skillsService) {
        this.skillsService = skillsService;
    }

    @GetMapping()
    public List<SkillsResponseDTO> getAllSkills() {
        return skillsService.getAllSkills();
    }

    @PostMapping
    public SkillsResponseDTO addSkill(@RequestBody SkillRequestDTO skill) {
        return skillsService.addSkill(skill);
    }

    @DeleteMapping("/{id}")
    public void deleteSkill(@PathVariable("id") int skillId) {
        skillsService.deleteSkill(skillId);
    }

}
