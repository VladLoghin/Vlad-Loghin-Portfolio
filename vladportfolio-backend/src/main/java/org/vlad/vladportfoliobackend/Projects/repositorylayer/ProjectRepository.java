package org.vlad.vladportfoliobackend.Projects.repositorylayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vlad.vladportfoliobackend.Projects.datalayer.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
