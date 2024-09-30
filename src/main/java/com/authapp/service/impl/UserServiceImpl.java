package com.authapp.service.impl;

import com.authapp.dto.UserDto;
import com.authapp.mapper.UserMapper;
import com.authapp.entity.Role;
import com.authapp.entity.User;
import com.authapp.repository.RoleRepository;
import com.authapp.repository.UserRepository;
import com.authapp.service.i.UserService;
import com.authapp.enums.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        try {
            User savedUser = userMapper.toEntity(userDto);
            savedUser = userRepository.save(savedUser);
            return userMapper.toDTO(savedUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto) {
        try {
            User existingUser = userRepository.findById(userDto.getId())
                    .orElseThrow(()-> new RuntimeException("User Not Found"));


            existingUser.setName(userDto.getName());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setUsername(userDto.getUsername());


            // Check whether the roles has been changed
            if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()){
                existingUser.getRoles().clear();
                for(UserRole r : userDto.getRoles()){
                    Role managedRole = roleRepository.findRoleByUserRole(r)
                            .orElseThrow(() -> new RuntimeException("Role not found"));
                    existingUser.getRoles().add(managedRole);
                }
            }

            User updatedUser = userRepository.save(existingUser);
            return userMapper.toDTO(updatedUser);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto getUserById(int id) {
        return userMapper.toDTO(userRepository.findById(id).orElseThrow(()-> new RuntimeException("User Not Found")));
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return userMapper.toDTO(userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User Not Found")));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        try {
            userRepository.delete(userRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("User Not Found")));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
