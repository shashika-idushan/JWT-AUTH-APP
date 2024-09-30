package com.authapp.controller;

import com.authapp.dto.LoginDto;
import com.authapp.dto.TokenResponse;
import com.authapp.dto.UserDto;
import com.authapp.entity.RefreshToken;
import com.authapp.entity.User;
import com.authapp.security.JwtTokenProvider;
import com.authapp.service.i.AuthService;
import com.authapp.service.i.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthenticationController {
    private static final Logger log = LogManager.getLogger(AuthenticationController.class);

    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        log.info("Authenticating : " + loginDto.getUsername());

        String token = authService.login(loginDto);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginDto.getUsername());

        // Set refresh token in HttpOnly cookie
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh-token")
                .maxAge(365 * 24 * 60 * 60) // 365 days
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(new TokenResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){
        String msg = authService.register(userDto);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refreshToken") String refreshToken) {
        User u = refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser).orElse(null);

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtTokenProvider.generateToken(user);
                    return ResponseEntity.ok(new TokenResponse(newAccessToken));
                })
                .orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));
    }

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh-token")
                .maxAge(0) // Delete the cookie
                .build();

        return ResponseEntity.ok().header("Set-Cookie", deleteCookie.toString()).build();
    }


}
