package com.example.v2exclone.dto;

import java.time.LocalDateTime;

public class TopicDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;
    private Integer replyCount;
    private UserDTO author;
    private NodeDTO node;
    
    // Constructors
    public TopicDTO() {}
    
    public TopicDTO(Long id, String title, String content, LocalDateTime createdAt, 
                   LocalDateTime updatedAt, Integer viewCount, Integer replyCount,
                   UserDTO author, NodeDTO node) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.replyCount = replyCount;
        this.author = author;
        this.node = node;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public Integer getReplyCount() {
        return replyCount;
    }
    
    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }
    
    public UserDTO getAuthor() {
        return author;
    }
    
    public void setAuthor(UserDTO author) {
        this.author = author;
    }
    
    public NodeDTO getNode() {
        return node;
    }
    
    public void setNode(NodeDTO node) {
        this.node = node;
    }
}
