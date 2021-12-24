package com.loginapp.demo.controller;

import com.loginapp.demo.dao.UserDao;
import com.loginapp.demo.jwt.JwtProvider;
import com.loginapp.demo.request.LoginRequest;
import com.loginapp.demo.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;


@Controller
public class LoginController {

    @Autowired
    private UserDao userDao;
    @Value("${secret}")
    private String secret;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) throws Exception {
        Logger.getAnonymousLogger().info(loginRequest.getUsername());
        Logger.getAnonymousLogger().info(loginRequest.getPassword());

            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getUsername(), loginRequest.getPassword()
                            )
                    );

            User user = (User) authenticate.getPrincipal();
            Logger.getAnonymousLogger().info(user.toString());
            return new ResponseEntity(
                            new LoginResponse(jwtProvider.generateJWT(user.getUsername(), secret, 90000000)),
                            HttpStatus.CREATED
                    );

    }

}
