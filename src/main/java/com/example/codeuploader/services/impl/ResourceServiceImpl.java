package com.example.codeuploader.services.impl;

import com.example.codeuploader.model.Resource;
import com.example.codeuploader.repositories.ResourceRepository;
import com.example.codeuploader.services.ResourceService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ResourceServiceImpl implements ResourceService {

    private ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public List<Resource> searchResource(String query) {
        List<Resource> resources = resourceRepository.searchResource(query);
        return resources;
    }
}
