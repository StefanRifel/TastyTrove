package com.tastytrove.repository;

import com.tastytrove.entity.Role;
import com.tastytrove.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByUserRole(UserRole role);
}
