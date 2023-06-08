package com.upc.leadyourway.repository;

import com.upc.leadyourway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserEmail(String user_email);
    User findByUserEmailAndUserPassword(String user_email, String user_password);
    boolean existsById(Long user_id);
    boolean existsByUserEmail(String user_email);
}
