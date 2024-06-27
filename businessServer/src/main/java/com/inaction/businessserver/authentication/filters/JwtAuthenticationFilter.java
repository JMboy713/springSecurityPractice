package com.inaction.businessserver.authentication.filters;

import com.inaction.businessserver.authentication.UsernamePasswordAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signing.key}")
    private String signingKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();// 토큰을 구문 분석해 클레임을 얻고 서명을 검증한다. 서명이 유효하지 않으면 예외 투척
        String username = claims.get("username", String.class);
        GrantedAuthority a = new SimpleGrantedAuthority("user");
        var auth  = new UsernamePasswordAuthentication(username, null, List.of(a));// auth 에 유저 이름의 권한을 부여한다.
        SecurityContextHolder.getContext().setAuthentication(auth);// SecurityContextHolder 에 auth 를 설정한다.
        filterChain.doFilter(request, response);// 필터체인의 다음 필터를 호출
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login"); //login 에 대해서는 요청이 필터되지 않게 구성
    }
}
