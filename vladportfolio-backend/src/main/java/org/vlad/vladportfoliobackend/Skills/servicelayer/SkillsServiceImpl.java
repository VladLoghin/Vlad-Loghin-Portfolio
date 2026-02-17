package org.vlad.vladportfoliobackend.Skills.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillRequestDTO;
import org.vlad.vladportfoliobackend.Skills.datalayer.Skills;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillsResponseDTO;
import org.vlad.vladportfoliobackend.Skills.repositorylayer.SkillsRepository;
import org.vlad.vladportfoliobackend.utils.JsonUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SkillsServiceImpl implements SkillsService {

    private final SkillsRepository skillsRepository;

    public SkillsServiceImpl(SkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    @Override
    public Flux<SkillsResponseDTO> getAllSkills() {
        return skillsRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .map(SkillsResponseDTO::from);
    }

    @Override
    public Mono<SkillsResponseDTO> addSkill(SkillRequestDTO skill) {
        return skillsRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .map(Skills::getDisplayOrder)
                .defaultIfEmpty(0)
                .reduce(Math::max)
                .flatMap(maxOrder -> {
                    Skills newSkill = new Skills();
                    newSkill.setSkillName(skill.getSkillName());
                    newSkill.setSubskills(JsonUtils.toJson(skill.getSubskills()));
                    newSkill.setDisplayOrder(maxOrder + 1);
                    return skillsRepository.save(newSkill);
                })
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

    @Override
    public Mono<Void> reorder(List<Integer> orderedIds) {
        return Flux.fromIterable(orderedIds)
                .index()
                .flatMap(tuple -> skillsRepository.findById(tuple.getT2())
                        .flatMap(s -> {
                            s.setDisplayOrder(tuple.getT1().intValue());
                            return skillsRepository.save(s);
                        }))
                .then();
    }
}
