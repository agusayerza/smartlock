package com.smartlock.server.security.jwt;

import com.smartlock.server.security.service.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;

@Component
@EnableScheduling
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    //@Value("${backend.app.JWTSecretKey}")
    private final String jwtSecret = "justASimpleJWTKey";

    //@Value("${backend.app.JWTExpiration}")
    private final int jwtExpiration = 84600;

    private HashSet<String> blackList = new HashSet<>();

    public String generateJwtToken(Authentication authentication) {

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        logger.info("Issuing JWT to user with id: " + userPrincipal.getId());
        return Jwts.builder()
		                .setSubject((userPrincipal.getUsername()))
		                .setIssuedAt(new Date())
		                .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
		                .signWith(SignatureAlgorithm.HS512, jwtSecret)
		                .compact();
    }

    public void blacklistJWT(String authToken){
        blackList.add(authToken);
    }

    @Scheduled(fixedRate = 30000)
    public void deleteExpiredBlacklistTokens() {
        for (String authToken : blackList) {
            try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            } catch (ExpiredJwtException e) {
                blackList.remove(authToken);
            }
        }
    }

    boolean validateJwtToken(String authToken) {
        if(! blackList.contains(authToken)) {
            try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
                return true;
            } catch (SignatureException e) {
                logger.error("Invalid JWT signature -> Message: {} ", e.getMessage());
            } catch (MalformedJwtException e) {
                logger.error("Invalid JWT token -> Message: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.error("Expired JWT token -> Message: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                logger.error("Unsupported JWT token -> Message: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                logger.error("JWT claims string is empty -> Message: {}", e.getMessage());
            }
        }

        return false;
    }
    
    String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
			                .setSigningKey(jwtSecret)
			                .parseClaimsJws(token)
			                .getBody().getSubject();
    }
}