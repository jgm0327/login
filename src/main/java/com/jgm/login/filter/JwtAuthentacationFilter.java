package com.jgm.login.filter;

import com.jgm.login.repository.UserRepository;
import com.jgm.login.token.TokenServiceImpl;
import com.jgm.login.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthentacationFilter extends BasicAuthenticationFilter {
    private final TokenServiceImpl tokenService;
    @Autowired
    private final UserRepository userRepository;

    public JwtAuthentacationFilter(AuthenticationManager authenticationManager,
                                   TokenServiceImpl tokenService,
                                   UserRepository userRepository) {
        super(authenticationManager);
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = tokenService.resolveToken(request);

        if (jwtHeader.equals("")) {
            chain.doFilter(request, response);
            return;
        }
        if (tokenService.validateToken(jwtHeader)) {
            String username = tokenService.getSubject(jwtHeader);
            User user = userRepository.findByUsername(username);
            if (user != null) {
                Authentication authentication = tokenService.getAuthentication(jwtHeader);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
                chain.doFilter(request, response);
            }
        }
    }
}
