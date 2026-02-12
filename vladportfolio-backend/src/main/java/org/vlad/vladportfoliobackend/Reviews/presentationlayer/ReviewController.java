package org.vlad.vladportfoliobackend.Reviews.presentationlayer;

import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewRequestDTO;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewResponseDTO;
import org.vlad.vladportfoliobackend.Reviews.servicelayer.ReviewService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping()
    public Flux<ReviewResponseDTO> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @PostMapping()
    public Mono<ReviewResponseDTO> addReview(@RequestBody ReviewRequestDTO review) {
        return reviewService.addReview(review);
    }

    @PatchMapping("/{id}/approved")
    public Mono<Void> changeReviewVisibility(@PathVariable("id") Long reviewId, @RequestParam boolean approved) {
        return reviewService.changeReviewVisibility(reviewId, approved);
    }

}
