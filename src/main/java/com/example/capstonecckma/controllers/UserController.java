package com.example.capstonecckma.controllers;

import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import com.example.capstonecckma.model.User;
import com.example.capstonecckma.services.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    // =================== user CREATE/REGISTER

    @GetMapping("/signup")
    public String showSignupForm(Model vModel){
        vModel.addAttribute("user", new User());
        return "users/signup";
    }

    @PostMapping("/users/signup")
    public String saveUser(@ModelAttribute User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        String emailSubject = "A new CodeUpLoader account has been created!";
        String emailBlurb = "Thank you for creating your new account in CodeUpLoader!\r\n\r\nThe username submitted was\r\n["
                + user.getUsername() + "].\r\nIf this was not expected, please contact customer support.";
        String emailTo = user.getEmail();

        userDao.save(user);
        emailService.prepareAndSend(emailSubject, emailBlurb, emailTo);
        return "redirect:/login";
    }

}
