package com.authapp.dto;

import com.authapp.enums.CommonStatus;
import com.authapp.enums.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * DTO for {@link com.authapp.entity.User}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    @NotNull(message = "Cannot be null")
    @NotEmpty(message = "Cannot be empty")
    private String username;
    @NotNull(message = "Cannot be null")
    @NotEmpty(message = "Cannot be empty")
    private String password;
    private CommonStatus status;
    private List<UserRole> roles;
}