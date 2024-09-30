package com.authapp.service.impl;

import com.authapp.dto.LoginDto;
import com.authapp.dto.UserDto;
import com.authapp.mapper.UserMapper;
import com.authapp.repository.RoleRepository;
import com.authapp.repository.UserRepository;
import com.authapp.security.JwtTokenProvider;
import com.authapp.service.i.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(UserDto userDto) {
        userRepository.save(userMapper.toEntity(userDto));
        return "Success";
    }
}
