package com.example.capstonecckma.controllers;

import com.example.capstonecckma.model.User;
import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
//import com.example.capstonecckma.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private ResourceRepository resourceDao;
    private UserRepository userDao;
//    private PasswordEncoder passwordEncoder;
//    private EmailService emailService;

//    public UserController(ResourceRepository resourceDao, UserRepository userDao, PasswordEncoder passwordEncoder, EmailService emailService) {
//        this.resourceDao = resourceDao;
//        this.userDao = userDao;
//        this.passwordEncoder = passwordEncoder;
//        this.emailService= emailService;
//    }

    public UserController(ResourceRepository resourceDao, UserRepository userDao) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
//        this.emailService= emailService;
    }

    @GetMapping("/users/register")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/users/register")
    public String saveUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email) {
//        String hash = passwordEncoder.encode(user.getPassword());
//        user.setPassword(hash);
        User user = new User(username, password, email);
        String emailSubject = "A new CCKMA user account has been created!";
        String emailBlurb = "Thank you for creating your new account in CCKMA!\r\n\r\nThe username submitted was\r\n["
                + user.getUsername() + "].\r\nIf this was not expected, please contact customer support.";
        String emailTo = user.getEmail();

        userDao.save(user);
//        emailService.prepareAndSend(emailSubject, emailBlurb, emailTo);
        return "redirect:users/register";
    }

}
