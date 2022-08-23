package com.example.capstonecckma.services;

import com.example.capstonecckma.model.Resource;

import java.util.List;

public interface ResourceService {
    List<Resource> searchResource(String query);
}
