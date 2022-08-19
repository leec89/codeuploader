package com.example.capstonecckma.controllers;

import com.example.capstonecckma.model.Doc;
import com.example.capstonecckma.model.Resource;
import com.example.capstonecckma.repositories.DocRepository;
import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.services.DocStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class DocController {

    @Autowired
    private DocStorageService docStorageService;
    @Autowired
    private ResourceRepository resourceDao;

    @Autowired
    private DocRepository ob;
    @GetMapping("/multiupload")
    public String get(Model vModel) {
        List<Doc> docs = docStorageService.getFiles();
        vModel.addAttribute("docs", docs);
        return "multiupload";
    }

    @PostMapping("/uploadFiles")
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        for (MultipartFile file: files) {
            List<Resource> resourceList = resourceDao.findAll();
            Resource lastOne = resourceList.get(resourceList.size()-1);
            int resId = (int) lastOne.getId();
            docStorageService.saveFile(file, resId);
        }
        return "redirect:/resources";
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) {
        Doc doc = docStorageService.getFile(fileId).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
                .body(new ByteArrayResource(doc.getData()));
    }

    @GetMapping("/deleteFile/{resId}/{fileId}")
    public ResponseEntity<ByteArrayResource> deleteFile(@PathVariable Integer fileId, @PathVariable long resId) throws IOException, URISyntaxException {
        ob.deleteById(fileId);
        URI yahoo = new URI("/resources/"+resId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(yahoo);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);

    }

    @GetMapping("/upload")
    public String uploadForm(Model vModel) {
        vModel.addAttribute("doc", new Doc());
        return "resources/upload";
    }

}