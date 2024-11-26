package com.lvh.RentalBE.services;

import com.lvh.RentalBE.model.Reviews;
import com.lvh.RentalBE.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private  final UserService userService;
    public ReviewService(ReviewRepository reviewRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }

    public void saveReview(Reviews review) {
        reviewRepository.save(review);
    }

    public List<Reviews> finReviewsByHostId(Long hostId) {
        return reviewRepository.findReviewsByHostId(this.userService.findByID(hostId));
    }

    public Double averageRatingByHostId(Long hostId) {
        List<Reviews> reviews = finReviewsByHostId(hostId);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToDouble(Reviews::getRating)
                .average()
                .orElse(0.0);
    }

}
