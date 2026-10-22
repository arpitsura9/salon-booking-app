package com.arpit.service.impl;

import com.arpit.dto.ReviewRequest;
import com.arpit.dto.SalonDTO;
import com.arpit.dto.UserDTO;
import com.arpit.modal.Review;
import com.arpit.repository.ReviewRepository;
import com.arpit.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    @Override
    public Review createReview(ReviewRequest req, UserDTO user, SalonDTO salon) {

        Review review = new Review();
        review.setReviewText(req.getReviewText());
        review.setRating(req.getRating());
        review.setUserId(user.getId());
        review.setSalonId(salon.getId());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsBySalonId(Long salonId) {
        return reviewRepository.findBySalonId(salonId);
    }

    private Review  getReviewById(Long id) throws Exception {
        return reviewRepository.findById(id).orElseThrow(
                ()->new Exception("review not exist...")
        );
    }
    @Override
    public Review updateReview(ReviewRequest req, Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(!review.getUserId().equals(userId)){
            throw new Exception("you do not have permission to update review");
        }
        review.setReviewText(req.getReviewText());
        review.setRating(req.getRating());

        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(!review.getUserId().equals(userId)){
            throw new Exception("you do not have permission to delete review");
        }
        reviewRepository.delete(review);
    }
}
