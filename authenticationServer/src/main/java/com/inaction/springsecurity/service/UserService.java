package com.inaction.springsecurity.service;

import com.inaction.springsecurity.domain.Otp;
import com.inaction.springsecurity.domain.User;
import com.inaction.springsecurity.repository.OtpRepository;
import com.inaction.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    public void auth(User user){
        User userFromDb = userRepository.findByUsername(user.getUsername()).orElseThrow();
        if(passwordEncoder.matches(user.getPassword(), userFromDb.getPassword())){
            renewOtp(userFromDb);
        }else{
            throw new RuntimeException("Invalid login or password");
        }
    }

    private void renewOtp(User u) {
        String code = GenerateCodeUtil.generateCode();
        Optional<Otp> otp = otpRepository.findOtpByUsername(u.getUsername());// 유저 이름의 otp가 있는지 확인.
        if(otp.isPresent()) { // 있다면
            Otp otpFromDb = otp.get();
            otpFromDb.setCode(code);
        }else{
            Otp newotp = new Otp();
            newotp.setUsername(u.getUsername());
            newotp.setCode(code);
            otpRepository.save(newotp);
        }
    }

    public boolean check(Otp otpToValidate) {
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(otpToValidate.getUsername());
        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            if (otp.getCode().equals(otpToValidate.getCode())) {
                return true; // otp가 일치하면 true
            }
        }
        return false;

    }


}
