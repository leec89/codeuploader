package com.example.capstonecckma.services.impl;

import com.example.capstonecckma.model.Resource;
import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.services.ResourceService;
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
