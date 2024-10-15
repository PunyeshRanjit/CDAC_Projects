package com.destina.Controller;


import com.destina.model.Review;
import com.destina.Service.ReviewService;
import com.destina.Dto.ReviewDto;
import com.destina.Dto.ReviewRequestDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews() {
        List<ReviewDto> reviews = reviewService.getReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable("id") Long id) {
        ReviewDto review = reviewService.getReview(id);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/byPackage/{packageId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByPackage(@PathVariable("packageId") Long packageId) {
        List<ReviewDto> reviews = reviewService.getReviewsByPackage(packageId);
        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<String> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
    	System.out.println("Yes");
        reviewService.createReview(reviewRequestDto);
        return ResponseEntity.ok("Review Created Successfully");
    }

   
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateReview(@PathVariable("id") Long id, @RequestBody ReviewDto reviewDto) {
        reviewService.updateReview(id, reviewDto);
        return ResponseEntity.ok("Review Updated Successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review Deleted Successfully");
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<Object> getReviewsByAgentId(@PathVariable("agentId") Long agentId) {
        List<ReviewDto> reviews = reviewService.getReviewsByAgentId(agentId);
        if (reviews.isEmpty()) {
            return ResponseEntity.ok().body("No Reviews found for this agent.");
        }
        return ResponseEntity.ok(reviews);
    }
}

