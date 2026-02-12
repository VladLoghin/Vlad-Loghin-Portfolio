package org.vlad.vladportfoliobackend.About.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.About.datalayer.Section;
import reactor.core.publisher.Mono;

public interface SectionRepository extends ReactiveCrudRepository<Section, Long> {
    Mono<Section> findByTitle(String title);
}
