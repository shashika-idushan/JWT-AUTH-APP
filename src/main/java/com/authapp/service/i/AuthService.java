package com.authapp.service.i;

import com.authapp.dto.LoginDto;
import com.authapp.dto.UserDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(UserDto userDto);
}
