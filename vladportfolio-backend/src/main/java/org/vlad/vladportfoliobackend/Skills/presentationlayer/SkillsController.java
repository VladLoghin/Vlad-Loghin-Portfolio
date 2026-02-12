package org.vlad.vladportfoliobackend.Skills.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillRequestDTO;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillsResponseDTO;
import org.vlad.vladportfoliobackend.Skills.servicelayer.SkillsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/skills")
public class SkillsController {

    private final SkillsService skillsService;

    public SkillsController(SkillsService skillsService) {
        this.skillsService = skillsService;
    }

    @GetMapping()
    public Flux<SkillsResponseDTO> getAllSkills() {
        return skillsService.getAllSkills();
    }

    @PostMapping
    public Mono<SkillsResponseDTO> addSkill(@RequestBody SkillRequestDTO skill) {
        return skillsService.addSkill(skill);
    }

    @PutMapping("/{id}")
    public Mono<SkillsResponseDTO> updateSkill(@PathVariable("id") int skillId, @RequestBody SkillRequestDTO skill) {
        return skillsService.updateSkill(skillId, skill);
    }

    @PatchMapping("/{id}/active")
    public Mono<Void> toggleActive(@PathVariable("id") int skillId, @RequestParam boolean active) {
        return skillsService.toggleActive(skillId, active);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSkill(@PathVariable("id") int skillId) {
        return skillsService.deleteSkill(skillId);
    }
}
