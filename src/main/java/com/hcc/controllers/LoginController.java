package com.hcc.controllers;

import com.hcc.dto.AuthCredentialsRequest;
import com.hcc.dto.UserDto;
import com.hcc.entities.Authority;
import com.hcc.entities.User;
import com.hcc.enums.AuthorityEnum;
import com.hcc.exceptions.ResourceNotFoundException;
import com.hcc.repositories.AuthoritiesRepo;
import com.hcc.repositories.UserRepository;
import com.hcc.services.UserDetailServiceImpl;
import com.hcc.utils.CustomPasswordEncoder;
import com.hcc.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    UserRepository repository;

    @Autowired
    CustomPasswordEncoder passwordEncoder;

    @Autowired
    JWTUtils jwtUtils;

    @PostMapping("/login")
    public UserDto login(@RequestBody AuthCredentialsRequest authCredentialsRequest) {
        User userFromDb = (User) userDetailService.loadUserByUsername(authCredentialsRequest.getUsername());

        if (!passwordEncoder.getPasswordEncoder().matches(authCredentialsRequest.getPassword(), userFromDb.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        jwtUtils.generateToken(userFromDb);

        return new UserDto(userFromDb.getId(), userFromDb.getUsername(), jwtUtils.generateToken(userFromDb));
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody AuthCredentialsRequest request) {
        String encodedPassword = passwordEncoder.getPasswordEncoder().encode(request.getPassword());

        User user = new User(LocalDate.now(), request.getUsername(), encodedPassword, null);

        List<Authority> authorities = List.of(new Authority(AuthorityEnum.ROLE_LEARNER.toString(), user));

        user.setAuthorities(authorities);

        repository.save(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestBody UserDto userDto) {
        return jwtUtils.validateToken(userDto.getToken(),
                userDetailService.loadUserByUsername(userDto.getUsername()));
    }
}
