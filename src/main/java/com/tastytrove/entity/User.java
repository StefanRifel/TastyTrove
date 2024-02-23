package com.tastytrove.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tbl_user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userid"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "userid"))
    private Set<Role> userRoles;

    @Column(name = "enabled")
    private boolean enabled = false;

    public void addRole(Role role){
        if(userRoles == null){
            userRoles = new HashSet<>();
        }
        userRoles.add(role);
    }

    public void deleteRole(Role role){
        userRoles.remove(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        for(Role role: userRoles){
            list.add(new SimpleGrantedAuthority(role.toString()));
        }
        return list;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
