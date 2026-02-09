package org.vlad.vladportfoliobackend.About.repositorylayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vlad.vladportfoliobackend.About.datalayer.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    Section findByTitle(String sectionTitle);

}
