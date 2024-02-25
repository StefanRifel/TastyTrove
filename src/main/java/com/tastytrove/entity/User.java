package com.tastytrove.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @Column(name = "userid")
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "firstName")
    private String firstname;

    @Column(name = "lastName")
    private String lastname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 64)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userid"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "userid"))
    private Set<Role> roles;

    @Column(name = "enabled")
    private boolean enabled = false;

    public void addRole(Role role){
        if(roles == null){
            roles = new HashSet<>();
        }
        roles.add(role);
    }

    public void deleteRole(Role role){
        roles.remove(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for(Role role: roles){
            list.add(new SimpleGrantedAuthority(role.toString()));
        }
        log.info("getAuthorities: {}", list);
        return list;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        //todo
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //todo
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //todo
        return true;
    }

    @Override
    public boolean isEnabled() {
        //todo
        return true;
    }
}
