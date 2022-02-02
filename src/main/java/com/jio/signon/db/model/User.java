package com.jio.signon.db.model;

import javax.persistence.*;

import com.jio.signon.db.enums.Role;
import com.jio.signon.dto.request.user.CreateUserDto;

import javax.validation.constraints.*;

import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private String username;

    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String mobile;

    public User(CreateUserDto createUserDto) {
        username = createUserDto.getUsername();
        password = createUserDto.getPassword();
        role = createUserDto.getRole();
        email = createUserDto.getEmail();
        mobile = createUserDto.getMobile();
    }
}
