package com.tastytrove.repository;

import com.tastytrove.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    private User expectedUser;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setUserRole(UserRole.TEST_USER);

        expectedUser = User.builder()
                .username("john_doe")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("secure password")
                .build();

        expectedUser.addRole(role);

        Mockito.when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(expectedUser));
        Mockito.when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(expectedUser));
        Mockito.when(userRepository.save(expectedUser)).thenReturn(expectedUser);
    }

    @Test
    void saveUser() {
        User actualUser = userRepository.save(expectedUser);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByEmail() {
        String expected = "john.doe@example.com";
        Optional<User> user = userRepository.findByEmail(expected);

        assertThat(user.isPresent()).isTrue();
        assertEquals(expected, user.get().getEmail());
    }

    @Test
    void findByUsername() {
        String expected = "john_doe";
        Optional<User> user = userRepository.findByUsername(expected);

        assertThat(user.isPresent()).isTrue();
        assertEquals(expected, user.get().getUsername());
    }
}