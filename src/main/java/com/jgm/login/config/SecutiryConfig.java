package com.jgm.login.config;

import com.jgm.login.auth.PrincipalDetailsService;
import com.jgm.login.filter.AuthenticationFilter;
import com.jgm.login.filter.JwtAuthentacationFilter;
import com.jgm.login.filter.JwtFilter;
import com.jgm.login.repository.UserRepository;
import com.jgm.login.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecutiryConfig extends AbstractHttpConfigurer<SecutiryConfig, HttpSecurity> {

    private final CorsFilter corsFilter;

    private final TokenServiceImpl tokenService;

    private final PrincipalDetailsService principalDetailsService;

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .addFilterBefore(new JwtFilter(tokenService), SecurityContextHolderFilter.class)
                .addFilter(new JwtAuthentacationFilter(this.authenticationConfiguration().getAuthenticationManager(),
                        tokenService, userRepository))
                .addFilter(new AuthenticationFilter(this.authenticationConfiguration().getAuthenticationManager(),
                        tokenService))
                .authorizeHttpRequests()
                .antMatchers("/user").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin().disable();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationConfiguration authenticationConfiguration() {
        return new AuthenticationConfiguration();
    }
}
