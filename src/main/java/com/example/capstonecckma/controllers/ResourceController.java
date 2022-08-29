package com.example.capstonecckma.controllers;

import com.example.capstonecckma.model.*;
import com.example.capstonecckma.repositories.CommentRepository;
import com.example.capstonecckma.repositories.CurriculumTopicRepository;
import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import com.example.capstonecckma.services.EmailService;
import com.example.capstonecckma.services.SlackService;
import com.example.capstonecckma.services.DocStorageService;
import com.example.capstonecckma.services.ResourceService;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Controller
public class ResourceController {
    private ResourceRepository resourceDao;
    private UserRepository userDao;

    private CurriculumTopicRepository curriculumTopicDao;

    private DocStorageService docStorageService;
    private final EmailService emailService;
    private final SlackService slackService;

    private ResourceService resourceService;


    @Autowired
    private CommentRepository commentRepository;

    public ResourceController(ResourceRepository resourceDao, UserRepository userDao, CurriculumTopicRepository curriculumTopicDao, DocStorageService docStorageService, EmailService emailService, SlackService slackService, ResourceService resourceService, CommentRepository commentRepository) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.curriculumTopicDao = curriculumTopicDao;
        this.docStorageService = docStorageService;
        this.emailService = emailService;
        this.slackService = slackService;
        this.resourceService = resourceService;
        this.commentRepository = commentRepository;
    }

// =================== Testing Pages

    @GetMapping("/testing")
    public String testPage(Model vModel) {
        return "testpage";
    }

    @GetMapping("/hello")
    ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    // =================== Initial landing on index URL

    @GetMapping("/")
    public String getIndex(Model vModel) {

        return "landingpage";
    }

    // =================== Landing page

    @GetMapping("/landing")
    public String getLanding(Model vModel) {

        return "landingpage";
    }

    // =================== resources view ALL (resources/showall.html)

    @GetMapping("/resources")
    public String getResources(Model vModel) {
        List<Resource> resourceList = resourceDao.findAll();
        // pass posts to view
        vModel.addAttribute("resources", resourceList);
        return "resources/showall";
    }

    // =================== resources view ONE (resources/showone.html)

    @GetMapping("/resources/{id}")
    public String getResource(@PathVariable("id") long id, Model vModel,  @PageableDefault(value=10) Pageable pageable) {
        Resource resource = resourceDao.findById(id).get();
        List<Doc> docs = docStorageService.getFiles();
        Page<Comment> comments = commentRepository.findByResourceId(id, pageable);
        vModel.addAttribute("docs", docs);
        vModel.addAttribute("resource", resource);
        vModel.addAttribute("curriculum", curriculumTopicDao);
        vModel.addAttribute("comment", new Comment());
        vModel.addAttribute("page", comments );
        return "resources/showone";
    }

    // =================== resource CREATE (resources/create.html)

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("resource", new Resource());
        List<CurriculumTopic> list = curriculumTopicDao.findAll();
        model.addAttribute("topic", list);
        return "resources/create";
    }

    @PostMapping("/resources/create")
    public String postCreateForm(@ModelAttribute Resource resource) throws SlackApiException, IOException, URISyntaxException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        resource.setUser(principal);
        String resourceTitle = resource.getTitle();
        String emailSubject = "A New Resource Has Been Added!";
        String emailBlurb = "Thank you for creating a new resource. The resource is titled \r\n["
                + resourceTitle + "].\r\nIf this was not expected, please contact customer support.";
        String emailTo = principal.getEmail();
        resourceDao.save(resource);
        emailService.prepareAndSend(emailSubject, emailBlurb, emailTo);
        String textToSlack = emailSubject + "\n" +
                "Title: " + resourceTitle + "\n" +
                "Message from CodeUpLoader :robot_face:";
        slackService.sendToSlack(textToSlack);
        return "multiupload";
    }

    // =================== resources EDIT/UPDATE (resources/edit.html)

    @GetMapping("/resources/{id}/edit")
    public String getEditForm(@PathVariable("id") long id, Model model) {
        Resource resource = resourceDao.findById(id).get();
        model.addAttribute("resource", resource);
        return "resources/edit";
    }

    @PostMapping("/resources/{id}/edit")
    public String postEditForm(@ModelAttribute Resource resource) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        resource.setUser(principal);
        resourceDao.save(resource);
        return "redirect:/resources";
    }

    // =================== resource DELETE

    @PostMapping("/resources/{id}/delete")
    public String deletePost(
            @PathVariable long id,
            @ModelAttribute Resource resource
    ) throws SlackApiException, IOException, URISyntaxException {

        Resource resourceToDelete = resourceDao.findById(resource.getId()).get();
        String resourceTitle = resourceToDelete.getTitle();
        String emailTo = resourceToDelete.getUser().getEmail();
        String emailSubject = "A CodeUploader resource has been deleted!";
        String emailBlurb = "A CodeUploader resource has been deleted!\r\n\r\nThe title of the deleted resource was\r\n[ " + resourceTitle + " ].\r\n If this was not expected, please contact customer support.";
        resourceDao.deleteById(id);
        emailService.prepareAndSend(emailSubject, emailBlurb, emailTo);
        String textToSlack = emailSubject + "\n" +
                "Title: " + resourceTitle + "\n" +
                "Message from CodeUpLoader :robot_face:";
        slackService.sendToSlack(textToSlack);

        return "redirect:/resources";
    }

    // =================== add single DOC to resource

    @GetMapping("/resources/individual-doc/{id}")
    public String resId(@PathVariable long id, Model model) {
        Resource r = resourceDao.findById(id).get();

        model.addAttribute("r", r);
        return "singleupload";
    }

    // =================== resources by TOPIC (resources/showbytopic.html)

    @GetMapping("/resources/topic/{topicId}")
    public String getTopic(@PathVariable long topicId, Model vModel) {
        CurriculumTopic topic = curriculumTopicDao.findById(topicId);
        List<Resource> resourceList = resourceDao.findAll();
        vModel.addAttribute("topics", topic);
        vModel.addAttribute("resources", resourceList);
        return "resources/showbytopic";
    }

    // =================== resources by SEARCH (resources/search.html)

    @GetMapping("/search")
    public String searchResource1(@RequestParam("query") String query, Model vModel){
        List<Resource> resourceList = resourceService.searchResource(query);
        vModel.addAttribute("resources", resourceList);
        vModel.addAttribute("queryValue", query);
        return "resources/search";
    }

    @GetMapping("/customHeader")
    ResponseEntity<String> customHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "foo");
        return new ResponseEntity<>(
                "Custom header set", headers, HttpStatus.OK);
    }

    @PostMapping("/resources/{id}/like")
    @ResponseBody
    public String likeUnlikePost(@PathVariable long id) {
        Resource r = resourceDao.findById(id).get();
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userThatLiked = userDao.findById(principal.getId()).get();
        r.toggleUserLike(userThatLiked);
        resourceDao.save(r);
        return "Post liked!";
    }

    @GetMapping("/do-stuff/{id}/{title}")
    public String slackSend(@PathVariable int id, @PathVariable String title) throws SlackApiException, IOException, URISyntaxException {
        String textToSlack = "Check out this resource about: " + "\n" +
                title + "\n" +
                "http://localhost:8080/resources/" + id + "\n" +
                "Message from CodeUpLoader :robot_face: :sparkling_heart:";
        slackService.sendToSlack(textToSlack);
        return "redirect:/resources/{id}";
    }

}


//    public String createComment(@ModelAttribute Comment comment, @PathVariable (value = "resourceId") Long resourceId) {
//
//        return null;



