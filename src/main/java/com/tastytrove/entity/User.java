package com.tastytrove.entity;

import com.tastytrove.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@NamedQuery(name = "User.findByEmail", query = "select u from User u where u.email=:email")

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userid;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private UserRole userRole;

    public User() {

    }

    public User(Long userid, String firstName, String lastName, String email, String password, UserRole userRole) {
        this.userid = userid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

}
