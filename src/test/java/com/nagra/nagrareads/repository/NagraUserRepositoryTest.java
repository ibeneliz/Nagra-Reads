package com.nagra.nagrareads.repository;

import com.nagra.nagrareads.model.NagraUser;
import com.nagra.nagrareads.model.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
}, showSql = true)
public class NagraUserRepositoryTest {

    @Autowired
    private NagraUserRepository nagraUserRepository;

    private NagraUser testUser;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        testUser = new NagraUser();
        testUser.setName("testuser");
        testUser.setUsername("testuser@gmail.com");
        testUser.setPassword("password");
        testUser.setUserType(UserType.ADMIN);
        nagraUserRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        nagraUserRepository.delete(testUser);
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundById() {
        NagraUser savedUser = nagraUserRepository.findById(testUser.getId()).orElse(null);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(testUser.getUsername(), savedUser.getUsername());
        Assertions.assertEquals(testUser.getPassword(), savedUser.getPassword());
    }

    @Test
    void givenUser_whenUpdated_thenCanBeFoundByIdWithUpdatedData() {
        testUser.setUsername("updatedUsername");
        nagraUserRepository.save(testUser);

        NagraUser updatedUser = nagraUserRepository.findById(testUser.getId()).orElse(null);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("updatedUsername", updatedUser.getUsername());
    }

    @Test
    void givenUser_whenFindByUsernameCalled_thenUserIsFound() {
        NagraUser foundUser = nagraUserRepository.findByUsername("testuser@gmail.com").orElse(new NagraUser());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("testuser@gmail.com", foundUser.getUsername());
    }
}