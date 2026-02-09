package org.vlad.vladportfoliobackend.Education.repositorylayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vlad.vladportfoliobackend.Education.datalayer.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
}
