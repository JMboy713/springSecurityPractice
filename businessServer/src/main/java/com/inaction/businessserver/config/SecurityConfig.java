package com.inaction.businessserver.config;

import com.inaction.businessserver.authentication.filters.InitialAuthenticationFilter;
import com.inaction.businessserver.authentication.filters.JwtAuthenticationFilter;
import com.inaction.businessserver.authentication.providers.OtpAuthenticationProvider;
import com.inaction.businessserver.authentication.providers.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OtpAuthenticationProvider otpAuthenticationProvider;

    @Autowired
    private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(otpAuthenticationProvider)
                .authenticationProvider(usernamePasswordAuthenticationProvider); // 여러 인증 프로바이더를 설정
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterAt(initialAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        http.authorizeRequests().anyRequest().authenticated(); // 모든 요청이 인증되게 한다.
    }

    @Bean
    public InitialAuthenticationFilter initialAuthenticationFilter() throws Exception {
        return new InitialAuthenticationFilter(authenticationManager(), "Lz8QmgJhRkI/W7lsN4j8zK9s2/AuZwXQf1N84LwZepg="); // Secret Key
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
