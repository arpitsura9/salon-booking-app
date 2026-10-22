package com.arpit.dto;

import lombok.Data;

@Data
public class ReviewRequest {

    private String reviewText;

    private Double rating;
}
