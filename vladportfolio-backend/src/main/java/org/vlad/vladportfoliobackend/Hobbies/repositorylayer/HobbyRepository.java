package org.vlad.vladportfoliobackend.Hobbies.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.Hobby;
import reactor.core.publisher.Flux;

public interface HobbyRepository extends ReactiveCrudRepository<Hobby, Long> {
    Flux<Hobby> findAllByOrderByIdAsc();
}
