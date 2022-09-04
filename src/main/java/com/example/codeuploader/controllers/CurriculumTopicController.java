package com.example.codeuploader.controllers;

import com.example.codeuploader.repositories.CurriculumTopicRepository;
import org.springframework.stereotype.Controller;

@Controller
public class CurriculumTopicController {

    private CurriculumTopicRepository curriculumTopicDao;

    public CurriculumTopicController(CurriculumTopicRepository curriculumTopicDao) {
        this.curriculumTopicDao = curriculumTopicDao;
    }

//    @GetMapping("/topics")
//    public String getResources(Model vModel) {
//        List<CurriculumTopic> topicList = curriculumTopicDao.findAll();
//        // pass posts to view
//        vModel.addAttribute("topics", topicList);
//        return "topics";
//    }

}
