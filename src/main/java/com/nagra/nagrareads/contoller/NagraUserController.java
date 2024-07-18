package com.nagra.nagrareads.contoller;

import com.nagra.nagrareads.configuration.jwt.JwtService;
import com.nagra.nagrareads.configuration.jwt.LoginForm;
import com.nagra.nagrareads.configuration.jwt.NagraUserDetailService;
import com.nagra.nagrareads.model.NagraUser;
import com.nagra.nagrareads.repository.NagraUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
public class NagraUserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NagraUserRepository nagraUserRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private NagraUserDetailService nagraUserDetailService;

    @PostMapping("/register/user")
    public NagraUser createUser(@RequestBody NagraUser nagraUser) {
        nagraUser.setPassword(passwordEncoder.encode(nagraUser.getPassword()));
        return nagraUserRepository.save(nagraUser);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginForm.username(), loginForm.password()
        ));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(nagraUserDetailService.loadUserByUsername(loginForm.username()));
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
}
