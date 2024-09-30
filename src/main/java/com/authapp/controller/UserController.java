package com.authapp.controller;

import com.authapp.dto.UserDto;
import com.authapp.service.i.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.addUser(userDto);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
        userDto.setId(id);
        UserDto updatedUser = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable int id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

