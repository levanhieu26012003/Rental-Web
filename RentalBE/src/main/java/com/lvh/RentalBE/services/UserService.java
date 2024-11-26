package com.lvh.RentalBE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, Cloudinary cloudinary, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByUserRole(String userRole) {
        if (userRole == "" || userRole == null)
            return userRepository.findAll();
        else
            return userRepository.findByUserRole(userRole);
    }

    public void save(User user) {
        User existingUser = null;
        if (user.getId() != null) {
            existingUser = userRepository.findById(user.getId()).orElse(null);
        }

        if (user.getFile() != null && user.getFile().getSize() > 0) {
            try {
                Map res = this.cloudinary.uploader().upload(user.getFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                user.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.printf("Error   uploading file to Cloudinary", ex);
            }
        } else if (existingUser != null) {
            user.setAvatar(existingUser.getAvatar());
        }

        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByID(Long id) {
        return userRepository.findById(id).orElse(null);
    }



    @Transactional
    public void delete(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            if (user.getUserCollection() != null) {
                for (User followed : user.getUserCollection()) {
                    followed.getUserCollection1().remove(user); // Xóa mối quan hệ ở phía người được theo dõi
                }
            }

            if (user.getUserCollection1() != null) {
                for (User follower : user.getUserCollection1()) {
                    follower.getUserCollection().remove(user); // Xóa mối quan hệ ở phía người theo dõi
                }
            }

            userRepository.delete(user);
        }
    }
}
