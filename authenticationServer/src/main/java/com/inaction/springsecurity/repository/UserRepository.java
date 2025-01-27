package com.inaction.springsecurity.repository;

import com.inaction.springsecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByUsername(String username);


}
