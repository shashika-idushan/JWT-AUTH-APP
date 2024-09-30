package com.authapp.service.i;

import com.authapp.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto addUser(UserDto userDto);
    public UserDto updateUser(UserDto userDto);
    public UserDto getUserById(int id);
    public UserDto getUserByUsername(String username);
    public List<UserDto> getAllUsers();
    public void deleteUser(int id);
}
