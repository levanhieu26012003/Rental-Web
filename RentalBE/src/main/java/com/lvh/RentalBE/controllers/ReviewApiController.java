package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.model.Reviews;
import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.services.ReviewService;
import com.lvh.RentalBE.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewApiController {
    private final ReviewService reviewService;
    private final UserService userService;
    public ReviewApiController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/listReview/{HostId}")
    public ResponseEntity<List<Reviews>> getReviews(@PathVariable Long HostId) {
        try {
            return ResponseEntity.ok(reviewService.finReviewsByHostId(HostId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("")
    public ResponseEntity<String> createReview(@RequestBody Map<String, String> r) {
        try{
            Reviews review = new Reviews();
            review.setComment(r.get("comment"));
            review.setRating(Integer.parseInt(r.get("rating")));
            review.setHostId(userService.findByID(Long.parseLong(r.get("hostId"))));
            review.setTenantId(userService.findByID(Long.parseLong(r.get("tenantId"))));

            reviewService.saveReview(review);
            return ResponseEntity.ok("ĐÃ TẠO");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/avgRating/{HostId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long HostId) {
        try {
            return ResponseEntity.ok(reviewService.averageRatingByHostId(HostId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/unactive/{id}")
    public String unactive(@PathVariable(value = "id") Long id) {
        try {
            User u = userService.findByID(id);
            u.setActive(false);
            this.userService.save(u);
            return  "ok";
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return "fail";
    }
}

