package com.witalo.libraryapi.dto;

import com.witalo.libraryapi.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User")
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank(message = "Login is required")
    @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
    private String login;

    @Email(message = "It must be a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    private List<String> roles;


    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.roles = user.getRoles();
        this.email = user.getEmail();

    }
}