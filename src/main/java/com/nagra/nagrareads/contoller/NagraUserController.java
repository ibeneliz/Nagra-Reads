package com.nagra.nagrareads.contoller;

import com.nagra.nagrareads.configuration.jwt.JwtUtils;
import com.nagra.nagrareads.configuration.jwt.NagraUserDetailService;
import com.nagra.nagrareads.model.LoginCredentials;
import com.nagra.nagrareads.model.NagraUser;
import com.nagra.nagrareads.service.NagraUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class NagraUserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NagraUserService nagraUserService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private NagraUserDetailService nagraUserDetailService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid NagraUser nagraUser) {
        nagraUser.setPassword(passwordEncoder.encode(nagraUser.getPassword()));
        return new ResponseEntity<>(nagraUserService.save(nagraUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginCredentials loginCredentials) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginCredentials.getUsername(), loginCredentials.getPassword()
        ));
        if (authentication.isAuthenticated()) {
            return JwtUtils.generateToken(nagraUserDetailService.loadUserByUsername(loginCredentials.getUsername()));
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
}
