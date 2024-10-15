package com.destina.Service;




import com.destina.model.Review;
import com.destina.Repository.PackageRepository;
import com.destina.Repository.ReviewRepository;
import com.destina.Repository.UserRepository;
import com.destina.CustomException.ReviewNotFoundException;
import com.destina.Dto.NotificationDto;
import com.destina.Dto.ReviewDto;
import com.destina.Dto.ReviewRequestDto;
import com.destina.Service.EmailService;
import com.destina.Service.NotificationService;
import com.destina.model.User;
import com.destina.model.Package;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;
    
    @Transactional(readOnly = true)
    public List<ReviewDto> getReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(r -> new ReviewDto(
                        r.getReviewId(),
                        r.getPostTime(),
                        r.getContent(),
                        r.getCustomer().getId(),
                        r.getCustomer().getFirstName() + " " + r.getCustomer().getLastName(),
                        r.getaPackage().getPackageId(),
                        r.getaPackage().getTitle()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewDto getReview(Long id) {
        return reviewRepository.findById(id)
                .map(r -> new ReviewDto(
                        r.getReviewId(),
                        r.getPostTime(),
                        r.getContent(),
                        r.getCustomer().getId(),
                        r.getCustomer().getFirstName() + " " + r.getCustomer().getLastName(),
                        r.getaPackage().getPackageId(),
                        r.getaPackage().getTitle()
                ))
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByPackage(Long packageId) {
        return reviewRepository.findAll()
                .stream()
                .filter(r -> r.getaPackage().getPackageId().equals(packageId))
                .map(r -> new ReviewDto(
                        r.getReviewId(),
                        r.getPostTime(),
                        r.getContent(),
                        r.getCustomer().getId(),
                        r.getCustomer().getFirstName() + " " + r.getCustomer().getLastName(),
                        r.getaPackage().getPackageId(),
                        r.getaPackage().getTitle()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDto createReview(ReviewRequestDto reviewRequestDto) {
        
    	User customer = userRepository.findById(reviewRequestDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

            Package tourPackage = packageRepository.findById(reviewRequestDto.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

            // Create a new review entity
            Review review = new Review();
            review.setContent(reviewRequestDto.getContent());
            review.setCustomer(customer);
            review.setaPackage(tourPackage);
            review.setPostTime(LocalDateTime.now());
            // Save the review entity
//            reviewRepository.save(review);
  
    	
        Review savedReview = reviewRepository.save(review);
System.out.println(review);
        // Send email and save notification
        User user = review.getCustomer();
        Package pkg = review.getaPackage();
        String subject = "Thank you for your valuable review";
        String body = String.format("<html><body><h1>Hello, %s %s</h1><h2>We appreciate your feedback on %s.</h2></body></html>",
                user.getFirstName(), user.getLastName(), pkg.getTitle());
        emailService.sendEmail(user.getEmail(), subject, body);

        notificationService.saveNotification(subject, user.getId(), pkg.getAgent().getId());

        return new ReviewDto(
                savedReview.getReviewId(),
                savedReview.getPostTime(),
                savedReview.getContent(),
                savedReview.getCustomer().getId(),
                savedReview.getCustomer().getFirstName() + " " + savedReview.getCustomer().getLastName(),
                savedReview.getaPackage().getPackageId(),
                savedReview.getaPackage().getTitle()
        );
    }


    
    @Transactional
    public ReviewDto updateReview(Long id, ReviewDto reviewDto) {
        Review existingReview = reviewRepository.findById(id)
            .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        // Fetch customer and package based on IDs
        User customer = userRepository.findById(reviewDto.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        Package aPackage = packageRepository.findById(reviewDto.getPackageId())
            .orElseThrow(() -> new RuntimeException("Package not found"));

        // Update the review content, customer, and package
        existingReview.setContent(reviewDto.getContent());
        existingReview.setCustomer(customer);
        existingReview.setaPackage(aPackage);
        existingReview.setPostTime(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(existingReview);

        // Send notification
        notificationService.saveNotification("Review updated", customer.getId(), aPackage.getAgent().getId());

        return new ReviewDto(
                updatedReview.getReviewId(),
                updatedReview.getPostTime(),
                updatedReview.getContent(),
                updatedReview.getCustomer().getId(),
                updatedReview.getCustomer().getFirstName() + " " + updatedReview.getCustomer().getLastName(),
                updatedReview.getaPackage().getPackageId(),
                updatedReview.getaPackage().getTitle()
        );
    }


    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        reviewRepository.deleteById(id);

        // Send notification
        Package pkg = review.getaPackage();
        notificationService.saveNotification("Review Deleted", review.getCustomer().getId(), pkg.getAgent().getId());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByAgentId(Long agentId) {
        return reviewRepository.findAll()
                .stream()
                .filter(r -> r.getaPackage().getAgent().getId().equals(agentId))
                .map(r -> new ReviewDto(
                        r.getReviewId(),
                        r.getPostTime(),
                        r.getContent(),
                        r.getCustomer().getId(),
                        r.getCustomer().getFirstName() + " " + r.getCustomer().getLastName(),
                        r.getaPackage().getTitle(),
                        r.getaPackage().getLocation(),
                        r.getaPackage().getStartDate(),
                        r.getaPackage().getEndDate()
                ))
                .collect(Collectors.toList());
    }
}
