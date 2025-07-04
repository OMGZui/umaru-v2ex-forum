package com.example.v2exclone.controller;

import com.example.v2exclone.dto.TopicDTO;
import com.example.v2exclone.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/topics")
@CrossOrigin(origins = "http://localhost:3000")
public class TopicController {
    
    @Autowired
    private TopicService topicService;
    
    @GetMapping
    public ResponseEntity<Page<TopicDTO>> getAllTopics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<TopicDTO> topics = topicService.getAllTopics(page, size);
        return ResponseEntity.ok(topics);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<TopicDTO>> getRecentTopics() {
        List<TopicDTO> topics = topicService.getRecentTopics();
        return ResponseEntity.ok(topics);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable Long id) {
        Optional<TopicDTO> topic = topicService.getTopicById(id);
        return topic.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TopicDTO> createTopic(@RequestBody CreateTopicRequest request) {
        try {
            TopicDTO topic = topicService.createTopic(
                request.getTitle(),
                request.getContent(),
                request.getAuthorId(),
                request.getNodeId()
            );
            return ResponseEntity.ok(topic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Inner class for request body
    public static class CreateTopicRequest {
        private String title;
        private String content;
        private Long authorId;
        private Long nodeId;
        
        // Getters and Setters
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
        
        public Long getAuthorId() {
            return authorId;
        }
        
        public void setAuthorId(Long authorId) {
            this.authorId = authorId;
        }
        
        public Long getNodeId() {
            return nodeId;
        }
        
        public void setNodeId(Long nodeId) {
            this.nodeId = nodeId;
        }
    }
}
