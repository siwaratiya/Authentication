package com.exampleee.workchopsecurite.Services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.exampleee.workchopsecurite.Config.JwtUtils;
import com.exampleee.workchopsecurite.Dto.AuthenticationRequest;
import com.exampleee.workchopsecurite.Dto.AuthenticationResponse;
import com.exampleee.workchopsecurite.Dto.RegistrationRequest;
import com.exampleee.workchopsecurite.Entity.Role;
import com.exampleee.workchopsecurite.Entity.User;
import com.exampleee.workchopsecurite.Repository.RoleRepository;
import com.exampleee.workchopsecurite.Repository.UserRepository;
import com.exampleee.workchopsecurite.Services.AuthUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImp implements AuthUserService {

    //injection des dépendences
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    //méthode pour encoder un password
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse register(RegistrationRequest request) {
        User user = RegistrationRequest.toEntity(request);
        // encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user.setRole(UserRole.APPRENANT);
        // user.getRoles().add(null)
        /////////many rols
        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName("UTILISATEUR")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName("ADMINISTRATEUR")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "appr":
                        Role modRole = roleRepository.findByName("APPRENANT")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName("VISITEUR")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);


        User savedUser = repository.save(user);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getId()); // optional
        claims.put("fullName", savedUser.getFullname() ); // optional

        // generate a JWT token
        String token = jwtUtils.generateToken(savedUser, claims);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                //recherche email and password in database
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        final User user = repository.findByEmail(request.getEmail()).get();
        Map<String, Object> claims = new HashMap<>();
        //add userid and fullname
        claims.put("userId", user.getId()); // optional
        claims.put("fullName", user.getFullname() ); // optional
        // generate a JWT token
        String token = jwtUtils.generateToken(user, claims);
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();
    }

}