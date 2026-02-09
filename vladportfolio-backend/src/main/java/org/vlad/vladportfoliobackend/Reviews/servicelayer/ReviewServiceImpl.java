package org.vlad.vladportfoliobackend.Reviews.servicelayer;

import org.springframework.stereotype.Service;
import org.vlad.vladportfoliobackend.Reviews.datalayer.Review;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewRequestDTO;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewResponseDTO;
import org.vlad.vladportfoliobackend.Reviews.repositorylayer.ReviewRepository;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(ReviewResponseDTO::from)
                .toList();
    }

    @Override
    public ReviewResponseDTO addReview(ReviewRequestDTO review) {
        Review newReview = new Review();
        newReview.setReviewerName(review.getReviewerName());
        newReview.setContent(review.getContent());
        newReview.setRating(review.getRating());
        newReview.setApproved(false);
        Review saved = reviewRepository.save(newReview);
        return ReviewResponseDTO.from(saved);
    }

    @Override
    public void changeReviewVisibility(Long reviewId, boolean approved) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review with id " + reviewId + " not found"));
        review.setApproved(approved);
        reviewRepository.save(review);
    }
}
