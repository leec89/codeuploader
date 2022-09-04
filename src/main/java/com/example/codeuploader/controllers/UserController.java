package com.example.codeuploader.controllers;

import com.example.codeuploader.model.Resource;
import com.example.codeuploader.repositories.ResourceRepository;
import com.example.codeuploader.repositories.UserRepository;
import com.example.codeuploader.model.User;
import com.example.codeuploader.services.EmailService;
import com.example.codeuploader.services.SlackService;
import com.slack.api.methods.SlackApiException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class UserController {

    private ResourceRepository resourceDao;
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private SlackService slackService;

    public UserController(ResourceRepository resourceDao, UserRepository userDao, PasswordEncoder passwordEncoder, EmailService emailService, SlackService slackService) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.slackService = slackService;
    }

// =================== user CREATE/REGISTER

    @GetMapping("/signup")
    public String showSignupForm(Model vModel) {
        vModel.addAttribute("user", new User());
        return "users/signup";
    }

    @PostMapping("/users/signup")
    public String saveUser(@ModelAttribute User user) throws SlackApiException, IOException, URISyntaxException {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        String username = user.getUsername();
        String emailSubject = "A new CodeUpLoader User has joined!";
        String emailBlurb = "Thank you for creating your new account in CodeUpLoader!\r\n\r\nThe username submitted was\r\n["
                + username + "].\r\nIf this was not expected, please contact customer support.";
        String emailTo = user.getEmail();

        userDao.save(user);
        emailService.prepareAndSend(emailSubject, emailBlurb, emailTo);
        String textToSlack = emailSubject + "\n" +
                "Username: " + username + "\n" +
                "Message from CodeUpLoader :robot_face: :sparkling_heart:";
        slackService.sendToSlack(textToSlack);
        return "redirect:/login";
    }

    // =================== user SHOW/VIEW profile - view user profile

    @GetMapping("/profile")
    public String getUserPosts(Model vModel) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Resource> resource = resourceDao.findAll();
        vModel.addAttribute("user", principal);
        vModel.addAttribute("resources", resource);
        return "users/profile";
    }

    // =================== About Us view

    @GetMapping("/about")
    public String getAboutPage() {
        return "about";
    }

    @GetMapping("/landingpage")
    public String getLandingPage() {
        return "landingpage";
    }
}
