package com.arpit.modal;

import com.arpit.dto.ReviewDTO;
import com.arpit.dto.UserDTO;

public class ReviewMapper {

    public static ReviewDTO toDTO(Review review, UserDTO user){
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setReviewText(review.getReviewText());
        reviewDTO.setUser(user);
        reviewDTO.setRating(review.getRating());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        return reviewDTO;
    }
}
