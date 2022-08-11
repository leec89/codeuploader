package com.example.capstonecckma.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED NOT NULL")
    private long id;

    @ManyToOne
    @JoinColumn (name = "user_id")
    @JsonBackReference
    private User user;

    @Column(length=100, nullable = false)
    private String title;

    @Column(length=100)
    private String link;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn (name = "curriculum_topic_id")
    @JsonBackReference
    private String curriculum_topic;

    @Column (length = 100)
    private String file_name;

    @Column (length = 100)
    private String file_type;

    @Column
    private byte file;

    @Column
    private LocalDateTime created_at;

    public Resource() {
    }

    public Resource(User user, String title, String link, String description, String curriculum_topic, String file_name, String file_type, byte file, LocalDateTime created_at) {
        this.user = user;
        this.title = title;
        this.link = link;
        this.description = description;
        this.curriculum_topic = curriculum_topic;
        this.file_name = file_name;
        this.file_type = file_type;
        this.file = file;
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurriculum_topic() {
        return curriculum_topic;
    }

    public void setCurriculum_topic(String curriculum_topic) {
        this.curriculum_topic = curriculum_topic;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public byte getFile() {
        return file;
    }

    public void setFile(byte file) {
        this.file = file;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", curriculum_topic='" + curriculum_topic + '\'' +
                ", file_name='" + file_name + '\'' +
                ", file_type='" + file_type + '\'' +
                ", file=" + file +
                ", created_at=" + created_at +
                '}';
    }
}
