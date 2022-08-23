package com.jgm.login.token;

import com.jgm.login.auth.PrincipalDetails;
import com.jgm.login.auth.PrincipalDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    byte[] secretKey = DatatypeConverter.parseBase64Binary("aslfnaldfnldng32nlsdfnslaslfnaldfnldng32nlsdfn");

    private final PrincipalDetailsService principalDetailsService;

    @Override
    public String createToken(String subject, long ttlMillis, String roles) {
        if (ttlMillis <= 0) {
            throw new RuntimeException("만료 기간은 0보다 커야합니다.");
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("roles", roles);
        Key signatureKey = new SecretKeySpec(secretKey, signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(signatureKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
                .compact();
    }

    @Override
    public String getSubject(String token) {
        if(token == null){
            return null;
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // 토큰이 유효한지 검사
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("토큰의 기간이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    @Override
    public Authentication getAuthentication(String token) {
        String username = getSubject(token);
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(principalDetails.getUsername(),
                principalDetails.getPassword(), principalDetails.getAuthorities());
    }

}
