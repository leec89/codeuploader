package com.example.capstonecckma.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "curriculum_topic")
public class CurriculumTopic {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(columnDefinition = "INT UNSIGNED NOT NULL")
    private long id;
    @Column(length = 100, nullable = false, unique = true)
    private String title;
    @Column(length = 100, nullable = false, unique = true)
    private String description;

    @OneToMany(mappedBy = "curriculum_topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resource> resources = new HashSet<>();

    public CurriculumTopic() {
    }

    public CurriculumTopic(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CurriculumTopic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @OneToMany(mappedBy = "curriculum_topic")
    private Collection<Resource> resource;

    public Collection<Resource> getResource() {
        return resource;
    }

    public void setResource(Collection<Resource> resource) {
        this.resource = resource;
    }
}
