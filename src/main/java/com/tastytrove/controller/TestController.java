package com.tastytrove.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@EnableMethodSecurity
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess(){
        log.info("all Endpoint hit");
        return "public content";
    }

    @GetMapping("/user")
    //@PostAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public String userAccess(){
        log.info("user Endpoint hit");
        return "user content";
    }

    @GetMapping("/mod")
    //@PreAuthorize("hasRole('MODERATOR')")
    public String modAccess(){
        log.info("mod Endpoint hit");
        return "moderator content";
    }

    @GetMapping("/admin")
    //@PreAuthorize("hasRole('ADMIN')")
    public String adminAccess(){
        log.info("admin Endpoint hit");
        return "admin content";
    }
}
