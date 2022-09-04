package com.example.codeuploader.services;

import com.example.codeuploader.model.Doc;
import com.example.codeuploader.model.Resource;
import com.example.codeuploader.repositories.DocRepository;
import com.example.codeuploader.repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class DocStorageService {
    @Autowired
    private DocRepository docRepository;
    @Autowired
    private ResourceRepository resourceDao;

    public Doc saveFile(MultipartFile file, long resId) {
        String docname = file.getOriginalFilename();

//        Resource r = new Resource(1);
        Resource res = new Resource((int) resId);

        try {
            Doc doc = new Doc(docname, file.getContentType(), file.getBytes(), res);
            return docRepository.save(doc);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Optional<Doc> getFile(Integer fileId) {
        return docRepository.findById(fileId);
    }
    public List<Doc> getFiles() {
        return docRepository.findAll();
    }
}
