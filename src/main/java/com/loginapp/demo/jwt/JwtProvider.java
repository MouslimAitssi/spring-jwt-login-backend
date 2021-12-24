package com.loginapp.demo.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtProvider {
    @Value("${secret}")
    private String secret;

    public static HttpServletResponse addAuthorizedHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE,PATCH,HEAD");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With,"
                + "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
        response.addHeader("Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin," + " Access-Control-Allow-Credentials, Authorization");
        return response;
    }

    public static String generateJWT(String username, String secret, long expirationTime) {
        Logger.getAnonymousLogger().info(username);
        String jwt = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();
        return jwt;
    }

    public boolean validateJwt(String jwt) {
        Logger.getAnonymousLogger().info(jwt);
        Jws<Claims> parsedJwt = parseJwt(jwt, secret);
        if (!isExpired(parsedJwt.getBody().getExpiration())) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean ifOption(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return true;
        }
        return false;
    }

    public static String resolveJWT(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");
        if ((jwt != null) && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);

            return jwt;
        }
        return "";
    }

    public static Jws<Claims> parseJwt(String jwt, String secret) {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes())).parseClaimsJws(jwt);
    }

    private static boolean isExpired(Date date) {
        return date.before(new Date(System.currentTimeMillis())) ? true : false;
    }


}

