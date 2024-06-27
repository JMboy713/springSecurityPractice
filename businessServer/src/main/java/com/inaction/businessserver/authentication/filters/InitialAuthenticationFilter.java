package com.inaction.businessserver.authentication.filters;

import com.inaction.businessserver.authentication.OtpAuthentication;
import com.inaction.businessserver.authentication.UsernamePasswordAuthentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class InitialAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager manager;
    private final String signingKey;

    public InitialAuthenticationFilter(AuthenticationManager manager, @Value("${jwt.signing.key}") String signingKey) {
        this.manager = manager;
        this.signingKey = signingKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String code = request.getHeader("code");

        // 디버그용 로그 추가
        System.out.println("Filter executed for path: " + request.getServletPath());
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Code: " + code);

        if (code == null) {
            Authentication a = new UsernamePasswordAuthentication(username, password);
            manager.authenticate(a);


        }else{
            System.out.println("OTP 인증");
            Authentication a = new OtpAuthentication(username, code);
            manager.authenticate(a);
            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setClaims(Map.of("username",username)).signWith(key).compact();
            response.setHeader("Authorization", jwt); // 헤더에 jwt 를 설정한다.
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/login"); /// login 경로에만 이 필터를 적용
    }
}
