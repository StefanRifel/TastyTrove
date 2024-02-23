package com.tastytrove.repository;

import com.tastytrove.entity.Role;
import com.tastytrove.entity.User;
import com.tastytrove.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        HashSet<Role> roles = new HashSet<>();

        Role userRole = new Role();
        userRole.setUserRole(UserRole.USER);
        Role moderatorRole = new Role();
        moderatorRole.setUserRole(UserRole.MODERATOR);

        roles.add(userRole);
        roles.add(moderatorRole);

        User user = User.builder()
                .username("john_doe")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("securepassword")
                .userRoles(roles)
                .enabled(true)
                .build();

        Mockito.when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.ofNullable(user));
    }

    @Test
    void findByEmail() {
        String expected = "john.doe@example.com";
        Optional<User> user = userRepository.findByEmail(expected);

        user.ifPresent(value -> assertEquals(expected, value.getEmail()));
    }
}