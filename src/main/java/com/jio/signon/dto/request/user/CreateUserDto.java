package com.jio.signon.dto.request.user;

import javax.validation.constraints.*;

import com.jio.signon.db.enums.Role;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NotNull
    @Size(max = 100)
    String username;

    @NotNull
    String password;

    @Size(max = 10)
    String mobile;

    String email;

    @NotNull
    Role role;

}
