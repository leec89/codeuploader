package com.example.capstonecckma.controllers;

import com.example.capstonecckma.model.Resource;
import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import com.example.capstonecckma.services.EmailService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class ResourceController {
    private ResourceRepository resourceDao;
    private UserRepository userDao;
    private final EmailService emailService;

    public ResourceController(ResourceRepository resourceDao, UserRepository userDao, EmailService emailService) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.emailService = emailService;
    }

//    index page mapping
    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

//    view of all resources
    @GetMapping("/resources")
    public String getResources(Model model) {

        model.addAttribute("resources", resourceDao.findAll());

        return "resources/index";
    }



}
