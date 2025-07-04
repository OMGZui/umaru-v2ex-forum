package com.example.v2exclone.dto;

import java.time.LocalDateTime;

public class NodeDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private LocalDateTime createdAt;
    
    // Constructors
    public NodeDTO() {}
    
    public NodeDTO(Long id, String name, String slug, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
