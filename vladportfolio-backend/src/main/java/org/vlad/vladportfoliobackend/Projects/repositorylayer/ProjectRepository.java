package org.vlad.vladportfoliobackend.Projects.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.Projects.datalayer.Project;

public interface ProjectRepository extends ReactiveCrudRepository<Project, Long> {
}
