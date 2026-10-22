package com.arpit.service;

import com.arpit.dto.ReviewRequest;
import com.arpit.dto.SalonDTO;
import com.arpit.dto.UserDTO;
import com.arpit.modal.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(ReviewRequest req,
                        UserDTO user,
                        SalonDTO salon);


    List<Review> getReviewsBySalonId(Long salonId);

    Review updateReview(ReviewRequest req, Long reviewId, Long userId ) throws Exception;

    void deleteReview(Long reviewId,Long userId) throws Exception;
}
