package org.vlad.vladportfoliobackend.Education.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.Education.datalayer.Education;
import reactor.core.publisher.Flux;

public interface EducationRepository extends ReactiveCrudRepository<Education, Long> {
    Flux<Education> findAllByOrderByDisplayOrderAscIdAsc();
}
