package com.exampleee.workchopsecurite.Controller;

import com.exampleee.workchopsecurite.Dto.AuthenticationRequest;
import com.exampleee.workchopsecurite.Dto.AuthenticationResponse;
import com.exampleee.workchopsecurite.Dto.RegistrationRequest;
import com.exampleee.workchopsecurite.Entity.User;
import com.exampleee.workchopsecurite.Repository.UserRepository;
import com.exampleee.workchopsecurite.Services.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthUserService service;
    private  final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/listerUser")
    public List<User> getUsers(){
        return userRepository.findAll();
    }
}