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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class DocController {

    @Autowired
    private DocStorageService docStorageService;
    @Autowired
    private ResourceRepository resourceDao;
    @Autowired
    private DocRepository ob;

    // =================== uploading DOC(s) to resource (from create!)

    @GetMapping("/multiupload")
    public String get(Model vModel) {
        List<Doc> docs = docStorageService.getFiles();
        vModel.addAttribute("docs", docs);
        return "multiupload";
    }

    @PostMapping("/uploadFile/{id}")
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @PathVariable long id) {
        for (MultipartFile file: files) {
            List<Resource> resourceList = resourceDao.findAll();

            docStorageService.saveFile(file, id);
        }
        return "redirect:/resources/" + id;
    }

    // =================== uploading DOC(s) to resource (inside showone, not after create)

    @GetMapping("/multiupload/{resId}")
    public String uploadDocsFromRes(Model vModel, @PathVariable long resId) {
        List<Doc> docs = docStorageService.getFiles();
        Resource resource = resourceDao.findById(resId).get();
        vModel.addAttribute("docs", docs);
        vModel.addAttribute("resource", resource);
        return "multiuploadshowone";
    }

    @PostMapping("/uploadFiles/{resId}")
    public String uploadMultipleDocsFromRes(@RequestParam("files") MultipartFile[] files, @PathVariable long resId) {
        for (MultipartFile file: files) {
            docStorageService.saveFile(file, resId);
        }

        return "redirect:/resources/" + resId;
    }

    // =================== downloading a DOC after click DOWNLOAD link

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) {
        Doc doc = docStorageService.getFile(fileId).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
                .body(new ByteArrayResource(doc.getData()));
    }

    // =================== deleting an individual DOC, and refreshing showone page with updated view

    @GetMapping("/deleteFile/{resId}/{fileId}")
    public ResponseEntity<ByteArrayResource> deleteFile(@PathVariable Integer fileId, @PathVariable long resId, RedirectAttributes redirAttrs) throws IOException, URISyntaxException {
        ob.deleteById(fileId);
        URI yahoo = new URI("/resources/"+resId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(yahoo);
        redirAttrs.addFlashAttribute("success", "File deleted successfully.");
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    // =================== testing upload of a single DOC to resource (need to be logged in)

    @GetMapping("/upload")
    public String uploadForm(Model vModel) {
        vModel.addAttribute("doc", new Doc());
        return "resources/upload";
    }

}