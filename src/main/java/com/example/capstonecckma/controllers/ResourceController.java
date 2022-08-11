package com.example.capstonecckma.controllers;

import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import com.example.capstonecckma.services.EmailService;

public class ResourceController {
    private ResourceRepository resourceDao;
    private UserRepository userDao;
    private final EmailService emailService;

    public ResourceController(ResourceRepository resourceDao, UserRepository userDao, EmailService emailService) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.emailService = emailService;
    }

}
