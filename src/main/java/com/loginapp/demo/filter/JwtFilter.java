package com.loginapp.demo.filter;

import com.loginapp.demo.jwt.JwtProvider;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${secret}")
    private String secret;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!jwtProvider.ifOption(request, response)) {
            try {
                String jwt = jwtProvider.resolveJWT(request);

                if (!jwt.equals("")) {
                    Logger.getAnonymousLogger().info(jwt);
                    if (this.jwtProvider.validateJwt(jwt)) {
                        Logger.getAnonymousLogger().info("validation");
                        this.authUser(jwt);
                    }
                }
            } catch (SignatureException e) {
            } catch (MalformedJwtException e) {
            } catch (ExpiredJwtException e) {
            }

        }
        filterChain.doFilter(request, response);
    }


    private void authUser(String jwt) {
        Jws<Claims> parsedJwt = jwtProvider.parseJwt(jwt, secret);
        String username = (String) parsedJwt.getBody().get("sub");
        Logger.getAnonymousLogger().info(username);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>()));
        logger.info(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        logger.info(SecurityContextHolder.getContext().getAuthentication());
    }
}
