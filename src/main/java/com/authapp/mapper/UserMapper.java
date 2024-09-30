package com.authapp.mapper;

import com.authapp.dto.UserDto;
import com.authapp.entity.Role;
import com.authapp.entity.User;
import com.authapp.repository.RoleRepository;
import com.authapp.enums.CommonStatus;
import com.authapp.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public UserDto toDTO(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .status(user.getStatus())
                .roles(user.getRoles().stream().map(Role::getUserRole).toList())
                .build();
    }

    public User toEntity(UserDto userDto){

        Set<Role> managedRoles = new HashSet<>();

        for(UserRole r : userDto.getRoles()){
            Role managedRole = roleRepository.findRoleByUserRole(r)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            managedRoles.add(managedRole);
        }

        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(encoder.encode(userDto.getPassword()))
                .status(CommonStatus.ACTIVE)
                .roles(managedRoles)
                .build();
    }
}
