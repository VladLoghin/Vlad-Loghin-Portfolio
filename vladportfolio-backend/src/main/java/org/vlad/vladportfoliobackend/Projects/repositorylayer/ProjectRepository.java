package org.vlad.vladportfoliobackend.Projects.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.Projects.datalayer.Project;
import reactor.core.publisher.Flux;

public interface ProjectRepository extends ReactiveCrudRepository<Project, Long> {
    Flux<Project> findAllByOrderByIdAsc();
}
