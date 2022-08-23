package com.jgm.login.token;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface TokenService {

    String createToken(String subject, long ttlMillis, String role);

    String getSubject(String token);

    boolean validateToken(String token);

    String resolveToken(HttpServletRequest request);

    Authentication getAuthentication(String token);
}
