package com.example.codeuploader.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name="docs")
public class Doc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String docName;

    private String docType;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn (name = "resource_id")
    @JsonBackReference
    private Resource resource;

    public Doc() {}

    public Doc(Resource resource){
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Doc(String docName, String docType, byte[] data, Resource resource) {
        this.docName = docName;
        this.docType = docType;
        this.data = data;
        this.resource = resource;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}


