package com.vinaypabba.bankrest.controller;

import com.vinaypabba.bankrest.model.SignInRequest;
import com.vinaypabba.bankrest.model.SignUpRequest;
import com.vinaypabba.bankrest.service.UserAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Api(value = "User Controller", description = "API for User registration and login")
public class UserController {

    private final UserAuthService userAuthService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserAuthService userAuthService, AuthenticationManager authenticationManager) {
        this.userAuthService = userAuthService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Sign in into the account", response = Map.class)
    public ResponseEntity signInUser(@RequestBody @NonNull SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(userAuthService.signInUser(authentication));
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Register a new user", response = Map.class)
    public ResponseEntity registerUser(@RequestBody @NonNull SignUpRequest signUpRequest) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", Boolean.FALSE);
        response.put("message", "Unable to register this user");
        if(userAuthService.registerUser(signUpRequest)) {
            response.put("success", Boolean.TRUE);
            response.put("message", signUpRequest.getUsername() + " registered successfully");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
