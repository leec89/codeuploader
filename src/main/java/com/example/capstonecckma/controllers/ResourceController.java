package com.example.capstonecckma.controllers;

import com.example.capstonecckma.model.CurriculumTopic;
import com.example.capstonecckma.model.Doc;
import com.example.capstonecckma.model.Resource;
import com.example.capstonecckma.model.User;
import com.example.capstonecckma.repositories.CurriculumTopicRepository;
import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import com.example.capstonecckma.services.EmailService;
import com.example.capstonecckma.services.DocStorageService;
import com.example.capstonecckma.services.ResourceService;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
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

    private ResourceService resourceService;

    public ResourceController(ResourceRepository resourceDao, UserRepository userDao, CurriculumTopicRepository curriculumTopicDao, DocStorageService docStorageService, EmailService emailService, ResourceService resourceService) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.curriculumTopicDao = curriculumTopicDao;
        this.docStorageService = docStorageService;
        this.emailService = emailService;
        this.resourceService = resourceService;
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
    public String getResource(@PathVariable("id") long id, Model vModel) {
        Resource resource = resourceDao.findById(id).get();
        List<Doc> docs = docStorageService.getFiles();
        vModel.addAttribute("docs", docs);
        vModel.addAttribute("resource", resource);
        vModel.addAttribute("curriculum", curriculumTopicDao);
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
    public String postCreateForm(@ModelAttribute Resource resource) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        resource.setUser(principal);
        String emailSubject = "New Resource Added";
        String emailBlurb = "Thank you for uploading a new resource. The resource is titled \r\n["
                + resource.getTitle() + "].\r\nIf this was not expected, please contact customer support.";
        String emailTo = principal.getEmail();
        resourceDao.save(resource);
        emailService.prepareAndSend(emailSubject, emailBlurb, emailTo);
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
    ) {

        Resource resourceToDelete = resourceDao.findById(resource.getId()).get();
        String emailTo = resourceToDelete.getUser().getEmail();

        String emailSubject = "A CodeUploader resource has been deleted!";
        String emailBlurb = "A CodeUploader resource has been deleted!\r\n\r\nThe title of the deleted resource was\r\n[ " + resourceToDelete.getTitle() + " ].\r\n If this was not expected, please contact customer support.";
        resourceDao.deleteById(id);
        emailService.prepareAndSend(emailSubject, emailBlurb, emailTo);

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
    static ResponseEntity<ByteArrayResource>  publishMessage(@PathVariable int id, @PathVariable String title) throws SlackApiException, IOException, URISyntaxException {
        String text = "I believe this to be a relevant resource about: " + "\n" +
                title + "\n" +
                "http://localhost:8080/resources/" + id + "\n" +
                "Message from CodeUpLoader :robot_face: :sparkling_heart:";

        String id1 = "C03UG71J5T6";

        // you can get this instance via ctx.client() in a Bolt app
        var client = Slack.getInstance().methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");

            // Call the chat.postMessage method using the built-in WebClient
            var result = client.chatPostMessage(r -> r
                            // The token you used to initialize your app
                            .token("")
                            .channel(id1)
                            .text(text)
                    // You could also use a blocks[] array to send richer content
            );
            // Print result, which includes information about the message (like TS)
            logger.info("result {}", result);

        URI yahoo = new URI("/resources/"+id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(yahoo);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

}




