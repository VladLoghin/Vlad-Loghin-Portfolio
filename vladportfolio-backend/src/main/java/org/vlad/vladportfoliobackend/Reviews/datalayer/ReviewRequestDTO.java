package org.vlad.vladportfoliobackend.Reviews.datalayer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    private String reviewerName;
    private String content;
    private Rating rating;
}
