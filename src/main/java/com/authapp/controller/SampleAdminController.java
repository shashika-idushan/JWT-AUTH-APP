package com.authapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class SampleAdminController {

    @GetMapping("/hello")
    public ResponseEntity<?> helloAdmin(){
        return ResponseEntity.ok("Hello Admin");
    }

}
