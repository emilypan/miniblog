package com.blog.miniblog.service;

import com.blog.miniblog.common.ResultCode;
import com.blog.miniblog.dto.User;
import com.blog.miniblog.exception.AuthorizationException;
import com.blog.miniblog.dto.UserRole;
import com.blog.miniblog.util.Constants;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Set;

@Component
@Slf4j
public class JwtTokenService implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token.expire-length:3600000}")
    private int tokenExpireLength;

    public String generateToken(String email, Set<UserRole> roles) throws UnsupportedEncodingException {
        log.debug("generateToken {}, email {}",secretKey, email);
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenExpireLength);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8") )
                .compact();
    }

    public User parseJwtToken(String token) {
        log.debug("parseJwtToken token: {}",token);
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(Constants.SECRET.getBytes("UTF-8"))
                    .parseClaimsJws(token);

//            Date exp = jws.getBody().get("exp", Date.class);
//            Date iat = jws.getBody().get("iat", Date.class);
            String email = jws.getBody().getSubject();
            log.debug("email {}",email);
            User user = new User(email);
            return user;

        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            throw new AuthorizationException(ResultCode.JWT_INVALID, "Invalid JWT signature trace");
        } catch (MalformedJwtException | UnsupportedEncodingException e) {
            log.info("Invalid JWT token.");
            throw new AuthorizationException(ResultCode.JWT_INVALID, "Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new AuthorizationException(ResultCode.JWT_EXPIRED, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            throw new AuthorizationException(ResultCode.JWT_UNSUPPORTED, "Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new AuthorizationException(ResultCode.JWT_INVALID, "JWT token compact of handler are invalid");
        }
//        } catch (UnsupportedEncodingException e) {
//            throw new ResourceInvalidException(ResultCode.JWT_INVALID, "Failed to parse User JWT token");
//
//        } catch (JwtException | UnsupportedEncodingException e) {
//            log.error("Failed to parse User JWt token: {}", e);
//            //e.printStackTrace();
//            throw new AuthorizationException(ResultCode.JWT_INVALID, "Failed to parse User JWT token");
//        }
    }
}