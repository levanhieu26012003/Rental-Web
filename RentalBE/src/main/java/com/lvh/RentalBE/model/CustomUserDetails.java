package com.lvh.RentalBE.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getUserRole().isEmpty() ? "ROLE_USER" : user.getUserRole(); // Mặc định là ROLE_USER nếu không có role
        return List.of(new SimpleGrantedAuthority(role));
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public String getFullName() {
        return user.getFullname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Tùy chọn logic để kiểm tra tài khoản có hết hạn không
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Tùy chọn logic để kiểm tra tài khoản có bị khóa không
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Tùy chọn logic để kiểm tra thông tin xác thực có hết hạn không
    }

    @Override
    public boolean isEnabled() {
        return true; // Tùy chọn logic để kiểm tra tài khoản có được kích hoạt không
    }
}
