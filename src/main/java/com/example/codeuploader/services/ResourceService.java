package com.example.codeuploader.services;

import com.example.codeuploader.model.Resource;

import java.util.List;

public interface ResourceService {
    List<Resource> searchResource(String query);
}
