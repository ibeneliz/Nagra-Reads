package com.nagra.nagrareads.contoller;

import com.nagra.nagrareads.configuration.jwt.JwtService;
import com.nagra.nagrareads.configuration.jwt.NagraUserDetailService;
import com.nagra.nagrareads.model.LoginCredentials;
import com.nagra.nagrareads.model.NagraUser;
import com.nagra.nagrareads.service.NagraUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NagraUserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NagraUserService nagraUserService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private NagraUserDetailService nagraUserDetailService;

    @PostMapping("/register/user")
    public NagraUser createUser(@RequestBody @Valid NagraUser nagraUser) {
        nagraUser.setPassword(passwordEncoder.encode(nagraUser.getPassword()));
        return nagraUserService.save(nagraUser);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody @Valid LoginCredentials loginCredentials) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginCredentials.getUsername(), loginCredentials.getPassword()
        ));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(nagraUserDetailService.loadUserByUsername(loginCredentials.getUsername()));
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
}
