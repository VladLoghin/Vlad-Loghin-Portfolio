package org.vlad.vladportfoliobackend.Reviews.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Reviews.datalayer.Review;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewRequestDTO;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewResponseDTO;
import org.vlad.vladportfoliobackend.Reviews.repositorylayer.ReviewRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Flux<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .map(ReviewResponseDTO::from);
    }

    @Override
    public Mono<ReviewResponseDTO> addReview(ReviewRequestDTO request) {
        return reviewRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .map(Review::getDisplayOrder)
                .defaultIfEmpty(0)
                .reduce(Math::max)
                .flatMap(maxOrder -> {
                    Review newReview = new Review();
                    newReview.setReviewerName(request.getReviewerName());
                    newReview.setContent(request.getContent());
                    newReview.setRating(request.getRating().name());
                    newReview.setApproved(false);
                    newReview.setDisplayOrder(maxOrder + 1);
                    newReview.setCreatedAt(LocalDateTime.now());
                    return reviewRepository.save(newReview);
                })
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

    @Override
    public Mono<Void> reorder(List<Long> orderedIds) {
        return Flux.fromIterable(orderedIds)
                .index()
                .flatMap(tuple -> reviewRepository.findById(tuple.getT2())
                        .flatMap(r -> {
                            r.setDisplayOrder(tuple.getT1().intValue());
                            return reviewRepository.save(r);
                        }))
                .then();
    }

    @Override
    public Mono<Void> deleteReviewById(Long id) {
        return reviewRepository.deleteById(id);
    }
}
