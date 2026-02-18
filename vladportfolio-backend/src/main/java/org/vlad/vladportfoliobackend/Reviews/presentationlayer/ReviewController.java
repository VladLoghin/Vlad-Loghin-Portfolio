package org.vlad.vladportfoliobackend.Reviews.presentationlayer;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewRequestDTO;
import org.vlad.vladportfoliobackend.Reviews.datalayer.ReviewResponseDTO;
import org.vlad.vladportfoliobackend.Reviews.servicelayer.ReviewService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @PreAuthorize("hasRole('Admin')")
    @PatchMapping("/{id}/approved")
    public Mono<Void> changeReviewVisibility(@PathVariable("id") Long reviewId, @RequestParam boolean approved) {
        return reviewService.changeReviewVisibility(reviewId, approved);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/reorder")
    public Mono<Void> reorder(@RequestBody List<Long> orderedIds) {
        return reviewService.reorder(orderedIds);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteReviewById(@PathVariable Long id) {
        return reviewService.deleteReviewById(id);
    }
}
