package com.lvh.RentalBE.controllers;

import com.lvh.RentalBE.dto.UserReviewDTO;
import com.lvh.RentalBE.model.Motel;
import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.services.ReviewService;
import com.lvh.RentalBE.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/review")
public class ReviewController {
    private final UserService userService;
    private final ReviewService reviewService;

    public ReviewController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping()
    public String createView(Model model) {
        List<User> users = userService.findByUserRole("HOST");
        List<UserReviewDTO> userReviews = new ArrayList<>();
        for (User user : users) {
            // Tính trung bình đánh giá cho mỗi user
            Double averageRating = reviewService.averageRatingByHostId(user.getId());

            // Tạo đối tượng DTO
            UserReviewDTO dto = new UserReviewDTO(user.getId(), user.getUsername(), user.getFullname(), user.getAvatar(), averageRating, user.getActive());
            userReviews.add(dto);
        }
        model.addAttribute("userReviews", userReviews);
        return "reviews";
    }

    @GetMapping("/detailReview/{id}")
    public String updateView(Model model, @PathVariable(value = "id") Long id) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("reviews", this.reviewService.finReviewsByHostId(id));
        attributes.put("user", this.userService.findByID(id));
        model.addAttribute("attributes", attributes);
        return "review";
    }

    @GetMapping("/unactive/{username}")
    public String unactive(@PathVariable(value = "username") String username) {
        try {
            User u = userService.findByUsername(username);
            for (Motel m : u.getMotelCollection()) {
                if (m.getStatus() == "APPROVED") {
                    m.setActive(false);
                }
            }
            u.setActive(false);
            this.userService.save(u);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return "redirect:/review";
    }

    @GetMapping("/active/{username}")
    public String active(@PathVariable(value = "username") String username) {
        try {
            User u = userService.findByUsername(username);
            for (Motel m : u.getMotelCollection()) {
                if (m.getStatus() == "APPROVED") {
                    m.setActive(true);
                }
            }
            u.setActive(true);
            this.userService.save(u);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return "redirect:/review";
    }

}

