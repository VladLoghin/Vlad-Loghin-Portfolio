package org.vlad.vladportfoliobackend.Skills.repositorylayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vlad.vladportfoliobackend.Skills.datalayer.Skills;

public interface SkillsRepository extends JpaRepository<Skills, Integer> {
}
