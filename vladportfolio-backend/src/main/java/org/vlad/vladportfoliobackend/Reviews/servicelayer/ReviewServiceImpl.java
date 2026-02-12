package org.vlad.vladportfoliobackend.Reviews.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Reviews.datalayer.Review;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewRequestDTO;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewResponseDTO;
import org.vlad.vladportfoliobackend.Reviews.repositorylayer.ReviewRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Flux<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll()
                .map(ReviewResponseDTO::from);
    }

    @Override
    public Mono<ReviewResponseDTO> addReview(ReviewRequestDTO request) {
        Review newReview = new Review();
        newReview.setReviewerName(request.getReviewerName());
        newReview.setContent(request.getContent());
        newReview.setRating(request.getRating().name());
        newReview.setApproved(false);
        return reviewRepository.save(newReview)
                .map(ReviewResponseDTO::from);
    }

    @Override
    public Mono<Void> changeReviewVisibility(Long reviewId, boolean approved) {
        return reviewRepository.findById(reviewId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Review with id " + reviewId + " not found")))
                .flatMap(review -> {
                    review.setApproved(approved);
                    return reviewRepository.save(review);
                })
                .then();
    }
}
