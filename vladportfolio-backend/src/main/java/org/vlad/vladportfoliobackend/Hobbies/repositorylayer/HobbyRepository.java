package org.vlad.vladportfoliobackend.Hobbies.repositorylayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vlad.vladportfoliobackend.Hobbies.datalayer.Hobby;

@Repository
public interface HobbyRepository extends JpaRepository<Hobby, Long> {
}
