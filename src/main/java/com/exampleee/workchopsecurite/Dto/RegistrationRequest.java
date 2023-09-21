package com.exampleee.workchopsecurite.Dto;

import com.exampleee.workchopsecurite.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

    private String fullname;
    private String email;
    private String password;
    private Set<String> role;



    public static User toEntity(RegistrationRequest request) {
        return User.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}