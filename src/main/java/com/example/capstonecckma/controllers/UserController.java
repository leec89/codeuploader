package com.example.capstonecckma.controllers;

import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private ResourceRepository resourceDao;
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    public UserController(ResourceRepository resourceDao, UserRepository userDao, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService= emailService;
    }
}
