package com.nagra.nagrareads.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagra.nagrareads.configuration.jwt.NagraUserDetailService;
import com.nagra.nagrareads.model.NagraUser;
import com.nagra.nagrareads.model.UserType;
import com.nagra.nagrareads.service.NagraUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NagraUserController.class)
class NagraUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NagraUserService nagraUserService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private NagraUserDetailService nagraUserDetailService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void when_register_should_succeedWithCorrectRequest() throws Exception {
        NagraUser newUser = new NagraUser();
        newUser.setName("ADMIN");
        newUser.setUsername("ADMIN@gmail.com");
        newUser.setPassword("1234");
        newUser.setUserType(UserType.ADMIN);

        NagraUser savedUser = new NagraUser();
        savedUser.setId(1L);
        savedUser.setName(newUser.getName());
        savedUser.setUsername(newUser.getUsername());
        savedUser.setPassword(newUser.getPassword());
        savedUser.setUserType(newUser.getUserType());

        given(nagraUserService.save(any())).willReturn(savedUser);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "ADMIN",
                                    "username": "ADMIN@gmail.com",
                                    "password": "1234",
                                    "userType": "ADMIN"
                                }
                                """)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(newUser.getUsername())));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void when_register_should_failWithoutCorrectRequest() throws Exception {

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "ADMIN",
                                    "username": "ADMIN",
                                    "password": "1234",
                                    "userType": "ADMIN"
                                }
                                """)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username", is("Invalid email: Example is user@gmail.com")));
    }
}