package org.vlad.vladportfoliobackend.Reviews.repositorylayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vlad.vladportfoliobackend.Reviews.datalayer.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
