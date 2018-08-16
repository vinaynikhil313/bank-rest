package com.vinaypabba.bankrest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity loginUser() {

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
