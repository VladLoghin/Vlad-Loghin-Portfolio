package org.vlad.vladportfoliobackend.Skills.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.Skills.datalayer.Skills;

public interface SkillsRepository extends ReactiveCrudRepository<Skills, Integer> {
}
