package org.vlad.vladportfoliobackend.Skills.presentationlayer;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillRequestDTO;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillsResponseDTO;
import org.vlad.vladportfoliobackend.Skills.servicelayer.SkillsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @PreAuthorize("hasRole('Admin')")
    @PostMapping
    public Mono<SkillsResponseDTO> addSkill(@RequestBody SkillRequestDTO skill) {
        return skillsService.addSkill(skill);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{id}")
    public Mono<SkillsResponseDTO> updateSkill(@PathVariable("id") int skillId, @RequestBody SkillRequestDTO skill) {
        return skillsService.updateSkill(skillId, skill);
    }

    @PreAuthorize("hasRole('Admin')")
    @PatchMapping("/{id}/active")
    public Mono<Void> toggleActive(@PathVariable("id") int skillId, @RequestParam boolean active) {
        return skillsService.toggleActive(skillId, active);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteSkill(@PathVariable("id") int skillId) {
        return skillsService.deleteSkill(skillId);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/reorder")
    public Mono<Void> reorder(@RequestBody List<Integer> orderedIds) {
        return skillsService.reorder(orderedIds);
    }
}
