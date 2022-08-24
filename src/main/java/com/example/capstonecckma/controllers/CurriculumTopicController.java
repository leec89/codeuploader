package com.example.capstonecckma.controllers;

import com.example.capstonecckma.model.CurriculumTopic;
import com.example.capstonecckma.model.Resource;
import com.example.capstonecckma.repositories.CurriculumTopicRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
