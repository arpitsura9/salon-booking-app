package com.arpit.controller;


import com.arpit.dto.*;
import com.arpit.modal.Review;
import com.arpit.modal.ReviewMapper;
import com.arpit.service.ReviewService;
import com.arpit.service.client.SalonFeignClient;
import com.arpit.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    private final UserFeignClient userFeignClient;
    private final SalonFeignClient salonFeignClient;


    @PostMapping("/salon/{salonId}")
    public ResponseEntity<Review> createReviews(
            @PathVariable Long salonId,
            @RequestBody ReviewRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userFeignClient.getUserProfile(jwt).getBody();
        SalonDTO salon = salonFeignClient.getSalonById(salonId).getBody();

        Review review = reviewService.createReview(req, user, salon);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsBySalonId(
            @PathVariable Long salonId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        SalonDTO salon = salonFeignClient.getSalonById(salonId).getBody();

        List<Review> reviews = reviewService.getReviewsBySalonId(salon.getId());

        List<ReviewDTO> reviewDTOs = reviews.stream().map((review)->
            {
                UserDTO user = null;
                try{
                    user = userFeignClient.getUserById(review.getUserId()).getBody();
                }catch(Exception e){
                    throw new RuntimeException(e.getMessage());
                }
                return ReviewMapper.toDTO(review, user);
            }).toList();
        return ResponseEntity.ok(reviewDTOs);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userFeignClient.getUserProfile(jwt).getBody();

        Review reviews = reviewService.updateReview(
                req,
                reviewId,
                user.getId()
        );
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO user = userFeignClient.getUserProfile(jwt).getBody();

        reviewService.deleteReview(reviewId, user.getId());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Review has been deleted");

        return ResponseEntity.ok(apiResponse);
    }


}
