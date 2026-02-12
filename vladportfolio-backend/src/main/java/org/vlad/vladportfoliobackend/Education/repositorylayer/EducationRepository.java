package org.vlad.vladportfoliobackend.Education.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.Education.datalayer.Education;

public interface EducationRepository extends ReactiveCrudRepository<Education, Long> {
}
