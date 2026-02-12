package org.vlad.vladportfoliobackend.Skills.servicelayer;

import org.vlad.vladportfoliobackend.Skills.datalayer.SkillRequestDTO;
import org.vlad.vladportfoliobackend.Skills.datalayer.SkillsResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SkillsService {

    Flux<SkillsResponseDTO> getAllSkills();

    Mono<SkillsResponseDTO> addSkill(SkillRequestDTO skill);

    Mono<SkillsResponseDTO> updateSkill(int skillId, SkillRequestDTO skill);

    Mono<Void> deleteSkill(int skillId);

    Mono<Void> toggleActive(int skillId, boolean active);
}
