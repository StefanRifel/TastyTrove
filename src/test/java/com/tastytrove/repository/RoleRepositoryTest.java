package com.tastytrove.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class RoleRepositoryTest {

    //@MockBean
    @Autowired
    private RoleRepository roleRepository;

    private Role expectedRole;

    @BeforeEach
    void setUp() {
        expectedRole = new Role();
        expectedRole.setUserRole(UserRole.TEST_USER);
    }

    @Test
    void saveRole() {
        Role actualRole = roleRepository.save(expectedRole);

        assertEquals(expectedRole, actualRole);
    }

    @Test
    void findByUserRole() {
        roleRepository.save(expectedRole);

        Optional<Role> actualRole = roleRepository.findByUserRole(expectedRole.getUserRole());

        assertThat(actualRole.isPresent()).isTrue();
        assertEquals(expectedRole, actualRole.get());
    }
}