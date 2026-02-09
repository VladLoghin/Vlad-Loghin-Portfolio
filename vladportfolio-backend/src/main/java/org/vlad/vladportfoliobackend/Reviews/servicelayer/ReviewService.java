package org.vlad.vladportfoliobackend.Reviews.servicelayer;

import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewRequestDTO;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {

    List<ReviewResponseDTO> getAllReviews();

    ReviewResponseDTO addReview(ReviewRequestDTO review);

    void changeReviewVisibility(Long reviewId, boolean approved);
}
