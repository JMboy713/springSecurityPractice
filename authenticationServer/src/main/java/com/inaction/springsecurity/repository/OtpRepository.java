package com.inaction.springsecurity.repository;

import com.inaction.springsecurity.domain.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,String> {
    Optional<Otp> findOtpByUsername(String username);
}
