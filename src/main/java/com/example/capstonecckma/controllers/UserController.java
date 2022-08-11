package com.example.capstonecckma.controllers;

import com.example.capstonecckma.model.User;
import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import com.example.capstonecckma.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private ResourceRepository resourceDao;
    private UserRepository userDao;
//    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

//    public UserController(ResourceRepository resourceDao, UserRepository userDao, PasswordEncoder passwordEncoder, EmailService emailService) {
//        this.resourceDao = resourceDao;
//        this.userDao = userDao;
//        this.passwordEncoder = passwordEncoder;
//        this.emailService= emailService;
//    }

    public UserController(ResourceRepository resourceDao, UserRepository userDao, EmailService emailService) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.emailService= emailService;
    }

    @GetMapping("/register")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        String emailSubject = "A new CCKMA user account has been created!";
        String emailBlurb = "Thank you for creating your new account in CCKMA!\r\n\r\nThe username submitted was\r\n[" + user.getUsername() + "].\r\nIf this was not expected, please contact customer support.";
        String emailTo = user.getEmail();

        userDao.save(user);
        emailService.prepareAndSend(emailSubject, emailBlurb, emailTo);
        return "/login";
    }

}
