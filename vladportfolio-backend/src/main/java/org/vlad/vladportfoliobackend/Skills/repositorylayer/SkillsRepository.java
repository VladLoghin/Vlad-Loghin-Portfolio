package org.vlad.vladportfoliobackend.Skills.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.Skills.datalayer.Skills;
import reactor.core.publisher.Flux;

public interface SkillsRepository extends ReactiveCrudRepository<Skills, Integer> {
    Flux<Skills> findAllByOrderByDisplayOrderAscIdAsc();
}
