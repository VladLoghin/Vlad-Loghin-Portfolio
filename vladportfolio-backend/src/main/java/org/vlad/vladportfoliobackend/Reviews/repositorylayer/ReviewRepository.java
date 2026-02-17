package org.vlad.vladportfoliobackend.Reviews.repositorylayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vlad.vladportfoliobackend.Reviews.datalayer.Review;
import reactor.core.publisher.Flux;

public interface ReviewRepository extends ReactiveCrudRepository<Review, Long> {
    Flux<Review> findAllByOrderByDisplayOrderAscIdAsc();
}
