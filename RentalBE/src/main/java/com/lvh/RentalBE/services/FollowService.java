package com.lvh.RentalBE.services;

import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.repositories.UserRepository;
import org.hibernate.loader.ast.spi.CollectionLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    public void followUser(Long followerId, Long hostId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        // Nếu đã theo dõi thì không làm gì
        if (follower.getUserCollection().contains(host)) {
            throw new RuntimeException("Already following this user");
        }

        // Thêm người được theo dõi vào danh sách following
        follower.getUserCollection().add(host);
        userRepository.save(follower);
    }

    public void unfollowUser(Long followerId, Long hostId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new RuntimeException("User to unfollow not found"));

        if (!follower.getUserCollection().contains(host)) {
            throw new RuntimeException("Not following this user");
        }

        // Xóa người được theo dõi khỏi danh sách following
        follower.getUserCollection().remove(host);
        userRepository.save(follower);
    }

    public Collection<User> Followers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return  user.getUserCollection1();
    }

    public Collection<User> Hosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return  user.getUserCollection();
    }

    public Integer countByFollowersUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return (Integer) user.getUserCollection1().size();
    }

    public Integer countByHostsUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return (Integer) user.getUserCollection().size();
    }

}
