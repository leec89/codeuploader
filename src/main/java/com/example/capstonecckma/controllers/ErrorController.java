package com.example.capstonecckma.controllers;

import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import com.example.capstonecckma.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    private ResourceRepository resourceDao;
    private UserRepository userDao;
    private final EmailService emailService;

    public ErrorController(ResourceRepository resourceDao, UserRepository userDao, EmailService emailService) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.emailService = emailService;
    }

    @GetMapping("/error400")
    public String getError() {
        return "error-pages/4xx";
    }
}
