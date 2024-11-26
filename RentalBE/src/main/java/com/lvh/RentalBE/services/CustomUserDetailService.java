package com.lvh.RentalBE.services;

import com.lvh.RentalBE.model.User;
import com.lvh.RentalBE.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(username)
                    .password(user.getPassword())
                    .roles(user.getUserRole())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

}
