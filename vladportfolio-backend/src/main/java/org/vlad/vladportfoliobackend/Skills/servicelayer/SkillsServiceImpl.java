package org.vlad.vladportfoliobackend.Skills.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillRequestDTO;
import org.vlad.vladportfoliobackend.Skills.datalayer.Skills;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillsResponseDTO;
import org.vlad.vladportfoliobackend.Skills.repositorylayer.SkillsRepository;
import org.vlad.vladportfoliobackend.utils.JsonUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SkillsServiceImpl implements SkillsService {

    private final SkillsRepository skillsRepository;

    public SkillsServiceImpl(SkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    @Override
    public Flux<SkillsResponseDTO> getAllSkills() {
        return skillsRepository.findAll()
                .map(SkillsResponseDTO::from);
    }

    @Override
    public Mono<SkillsResponseDTO> addSkill(SkillRequestDTO skill) {
        Skills newSkill = new Skills();
        newSkill.setSkillName(skill.getSkillName());
        newSkill.setSubskills(JsonUtils.toJson(skill.getSubskills()));
        return skillsRepository.save(newSkill)
                .map(SkillsResponseDTO::from);
    }

    @Override
    public Mono<SkillsResponseDTO> updateSkill(int skillId, SkillRequestDTO dto) {
        return skillsRepository.findById(skillId)
                .switchIfEmpty(Mono.error(new RuntimeException("Skill not found: " + skillId)))
                .flatMap(existing -> {
                    existing.setSkillName(dto.getSkillName());
                    existing.setSubskills(JsonUtils.toJson(dto.getSubskills()));
                    return skillsRepository.save(existing);
                })
                .map(SkillsResponseDTO::from);
    }

    @Override
    public Mono<Void> deleteSkill(int skillId) {
        return skillsRepository.deleteById(skillId);
    }

    @Override
    public Mono<Void> toggleActive(int skillId, boolean active) {
        return skillsRepository.findById(skillId)
                .switchIfEmpty(Mono.error(new RuntimeException("Skill not found: " + skillId)))
                .flatMap(existing -> {
                    existing.setActive(active);
                    return skillsRepository.save(existing);
                })
                .then();
    }
}
