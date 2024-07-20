package com.nagra.nagrareads.service;

import com.nagra.nagrareads.model.NagraUser;
import com.nagra.nagrareads.repository.NagraUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NagraUserServiceTest {

    @Mock
    private NagraUserRepository nagraUserRepository;

    @InjectMocks
    private NagraUserService nagraUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTest() {
        NagraUser nagraUser = new NagraUser();
        nagraUser.setUsername("testUser");
        nagraUser.setPassword("password");

        when(nagraUserRepository.save(any(NagraUser.class))).thenReturn(nagraUser);

        NagraUser savedUser = nagraUserService.save(nagraUser);

        assertEquals("testUser", savedUser.getUsername());
        verify(nagraUserRepository, times(1)).save(nagraUser);
    }
}