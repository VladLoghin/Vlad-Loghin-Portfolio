package org.vlad.vladportfoliobackend.Reviews.datalayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private String id;
    private String name;
    private String role;
    private String avatar;
    private String content;
    private int rating;
    private boolean approved;

    public static ReviewResponseDTO from(Review review) {
        return new ReviewResponseDTO(
                String.valueOf(review.getId()),
                review.getReviewerName(),
                "Reviewer",
                "",
                review.getContent(),
                ratingToInt(review.getRating()),
                review.isApproved()
        );
    }

    private static int ratingToInt(String rating) {
        if (rating == null) return 0;
        return switch (rating) {
            case "ONE" -> 1;
            case "TWO" -> 2;
            case "THREE" -> 3;
            case "FOUR" -> 4;
            case "FIVE" -> 5;
            default -> 0;
        };
    }
}
