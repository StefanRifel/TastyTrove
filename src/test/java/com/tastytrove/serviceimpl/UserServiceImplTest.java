package com.tastytrove.serviceimpl;

import com.tastytrove.entity.User;
import com.tastytrove.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setUserRole(UserRole.TEST_USER);

        User expectedUser = User.builder()
                .username("john_doe")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("secure password")
                .build();

        expectedUser.addRole(role);

        Mockito.when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(expectedUser));
        Mockito.when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(expectedUser));
    }

    @Test
    void loadUserByEmail() {
        String email = "john.doe@example.com";
        Optional<User> user = userRepository.findByEmail("john.doe@example.com");

        assertThat(user.isPresent()).isTrue();
        assertEquals(email, user.get().getEmail());
    }

    @Test
    void loadUserByUsername() {
        String username = "john_doe";
        Optional<User> user = userRepository.findByUsername(username);

        assertThat(user.isPresent()).isTrue();
        assertEquals(username, user.get().getUsername());
    }
}
