package org.vlad.vladportfoliobackend.Reviews.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewRequestDTO;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewResponseDTO;
import org.vlad.vladportfoliobackend.Reviews.servicelayer.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping()
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @PostMapping()
    public ReviewResponseDTO addReview(@RequestBody ReviewRequestDTO review) {
        return reviewService.addReview(review);
    }

    @PatchMapping("/{id}/approved")
    public void changeReviewVisibility(@PathVariable("id") Long reviewId, @RequestParam boolean approved) {
        reviewService.changeReviewVisibility(reviewId, approved);
    }

}
