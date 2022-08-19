package com.example.capstonecckma.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.swing.text.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private CurriculumTopic curriculum_topic;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resource")
    @JsonManagedReference
    private List<Doc> docs = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @JsonIgnore
    private LocalDateTime created_at = LocalDateTime.now();

//    Many to many resource_likes
@ManyToMany(cascade = CascadeType.ALL)
@JoinTable(
        name="resource_likes",
        joinColumns={@JoinColumn(name="resource_id")},
        inverseJoinColumns={@JoinColumn(name="user_id")}
)
public List<User> usersThatLiked = new ArrayList<>();

    public Resource() {
    }

    public Resource(User user, String title, String link, String description, CurriculumTopic curriculum_topic, List<Doc> docs, LocalDateTime created_at) {
        this.user = user;
        this.title = title;
        this.link = link;
        this.description = description;
        this.curriculum_topic = curriculum_topic;
        this.docs = docs;
        this.created_at = created_at;
    }

    public Resource(int id) {
        this.id = id;
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

    public CurriculumTopic getCurriculum_topic() {
        return curriculum_topic;
    }

    public void setCurriculum_topic(CurriculumTopic curriculum_topic) {
        this.curriculum_topic = curriculum_topic;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public List<Doc> getDocs() {
        return docs;
    }

    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }

//  Getter and Setter for usersThatLiked
    public List<User> getUsersThatLiked() {
        return usersThatLiked;
    }

    public void setUsersThatLiked(List<User> usersThatLiked) {
        this.usersThatLiked = usersThatLiked;
    }

//    Methods for likes/favorite

    public boolean containsId(final List<User> list, final long id){
        return list.stream().map(User::getId).anyMatch(userId -> userId == id);
    }

    public void toggleUserLike(User user) {
        if (containsId(usersThatLiked, user.getId())) {
            usersThatLiked.remove(user);
        } else {
            usersThatLiked.add(user);
        }
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", curriculum_topic=" + curriculum_topic +
                ", docs=" + docs +
                ", created_at=" + created_at +
                '}';
    }
}