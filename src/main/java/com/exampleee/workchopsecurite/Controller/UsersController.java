package com.exampleee.workchopsecurite.Controller;

import com.exampleee.workchopsecurite.Entity.User;
import com.exampleee.workchopsecurite.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Users")
@RequiredArgsConstructor
public class UsersController {
    private  final UserRepository userRepository;
    @GetMapping("/listerUser")
    public List<User> getUsers(){
        return userRepository.findAll();
    }
}
