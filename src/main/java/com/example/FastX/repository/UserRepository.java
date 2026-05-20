package com.example.FastX.repository;

import com.example.FastX.constants.Role;
import com.example.FastX.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    List<User> findByRole(Role role);
    boolean existsByEmail(String email);
    User findByIdAndRole(int id, Role passenger);
    User findByEmailAndActiveTrue(String email);
}