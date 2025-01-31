package com.authapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class SampleUserController {

    @GetMapping("/hello")
    public ResponseEntity<?> helloUser(){
        return ResponseEntity.ok("Hello User");
    }
}
