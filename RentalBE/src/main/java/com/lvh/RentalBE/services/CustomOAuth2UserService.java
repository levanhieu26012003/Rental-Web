package com.lvh.RentalBE.services;

import com.lvh.RentalBE.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository; // Để lưu người dùng vào cơ sở dữ liệu

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
                String provider = userRequest.getClientRegistration().getRegistrationId();


        // Print the email if available
        System.out.println("Email: " + Optional.ofNullable(oAuth2User.getAttribute("email")).orElse("No email"));

        // Extract and print profile picture URL based on provider
        String profilePictureUrl = extractProfilePictureUrl(provider, oAuth2User);

        return oAuth2User;
    }

    private String extractProfilePictureUrl(String provider, OAuth2User oAuth2User) {
        if ("github".equals(provider)) {
            // GitHub profile picture URL is found in "avatar_url"
            return Optional.ofNullable(oAuth2User.getAttribute("avatar_url")).toString();
        } else if ("facebook".equals(provider)) {
            // Facebook profile picture URL is nested under "picture.data.url"
            Map<String, Object> pictureData = (Map<String, Object>) oAuth2User.getAttribute("picture");
            if (pictureData != null) {
                Map<String, Object> data = (Map<String, Object>) pictureData.get("data");
                return (String) (data != null ? data.get("url") : null);
            }
        }
        return "No profile picture";
    }
}
