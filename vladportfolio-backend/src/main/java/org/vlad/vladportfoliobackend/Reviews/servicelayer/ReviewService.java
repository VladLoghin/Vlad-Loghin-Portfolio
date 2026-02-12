package org.vlad.vladportfoliobackend.Reviews.servicelayer;

import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewRequestDTO;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {

    Flux<ReviewResponseDTO> getAllReviews();

    Mono<ReviewResponseDTO> addReview(ReviewRequestDTO review);

    Mono<Void> changeReviewVisibility(Long reviewId, boolean approved);
}
