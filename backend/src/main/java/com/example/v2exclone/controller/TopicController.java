package com.example.v2exclone.controller;

import com.example.v2exclone.dto.TopicDTO;
import com.example.v2exclone.entity.User;
import com.example.v2exclone.service.TopicService;
import com.example.v2exclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/topics")
@CrossOrigin(origins = "http://localhost:3000")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;
    
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
    public ResponseEntity<TopicDTO> createTopic(
            @RequestBody CreateTopicRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // 检查用户是否已登录
            if (userDetails == null) {
                return ResponseEntity.status(401).build();
            }

            // 从UserDetails中获取用户信息
            Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
            if (!userOptional.isPresent()) {
                return ResponseEntity.status(401).build();
            }

            User currentUser = userOptional.get();
            TopicDTO topic = topicService.createTopic(
                request.getTitle(),
                request.getContent(),
                currentUser.getId(),
                request.getNodeId()
            );
            return ResponseEntity.ok(topic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDTO> updateTopic(
            @PathVariable Long id,
            @RequestBody UpdateTopicRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // 检查用户是否已登录
            if (userDetails == null) {
                return ResponseEntity.status(401).build();
            }

            // 从UserDetails中获取用户信息
            Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
            if (!userOptional.isPresent()) {
                return ResponseEntity.status(401).build();
            }

            User currentUser = userOptional.get();

            // 检查主题是否存在以及用户是否有权限编辑
            Optional<TopicDTO> existingTopicOpt = topicService.getTopicById(id);
            if (!existingTopicOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            TopicDTO existingTopic = existingTopicOpt.get();
            if (!existingTopic.getAuthor().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).build(); // 禁止访问
            }

            TopicDTO updatedTopic = topicService.updateTopic(
                id,
                request.getTitle(),
                request.getContent(),
                request.getNodeId()
            );
            return ResponseEntity.ok(updatedTopic);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Inner class for request body
    @Data
    public static class CreateTopicRequest {
        private String title;
        private String content;
        private Long nodeId;
    }

    // Inner class for update request body
    @Data
    public static class UpdateTopicRequest {
        private String title;
        private String content;
        private Long nodeId;
    }
}
