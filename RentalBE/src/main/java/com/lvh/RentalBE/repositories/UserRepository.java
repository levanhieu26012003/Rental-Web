package com.lvh.RentalBE.repositories;

import com.lvh.RentalBE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    void deleteByUsername(String username);
    List<User> findByUserRole(String userRole);
}
