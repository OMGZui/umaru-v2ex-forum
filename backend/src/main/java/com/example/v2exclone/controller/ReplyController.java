package com.example.v2exclone.controller;

import com.example.v2exclone.dto.ReplyDTO;
import com.example.v2exclone.entity.User;
import com.example.v2exclone.service.ReplyService;
import com.example.v2exclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/replies")
@CrossOrigin(origins = "http://localhost:3000")
public class ReplyController {
    
    @Autowired
    private ReplyService replyService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<ReplyDTO>> getRepliesByTopic(@PathVariable Long topicId) {
        try {
            List<ReplyDTO> replies = replyService.getRepliesByTopic(topicId);
            return ResponseEntity.ok(replies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/topic/{topicId}/page")
    public ResponseEntity<Page<ReplyDTO>> getRepliesByTopicPaged(
            @PathVariable Long topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Page<ReplyDTO> replies = replyService.getRepliesByTopic(topicId, page, size);
            return ResponseEntity.ok(replies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ReplyDTO> createReply(
            @RequestBody CreateReplyRequest request,
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
            ReplyDTO reply = replyService.createReply(
                request.getContent(),
                currentUser.getId(),
                request.getTopicId()
            );
            return ResponseEntity.ok(reply);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<ReplyDTO>> getRepliesByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Page<ReplyDTO> replies = replyService.getRepliesByAuthor(authorId, page, size);
            return ResponseEntity.ok(replies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/topic/{topicId}/count")
    public ResponseEntity<Long> getReplyCountByTopic(@PathVariable Long topicId) {
        try {
            long count = replyService.getReplyCountByTopic(topicId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Inner class for request body
    public static class CreateReplyRequest {
        private String content;
        private Long topicId;
        
        // Getters and Setters
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
        
        public Long getTopicId() {
            return topicId;
        }
        
        public void setTopicId(Long topicId) {
            this.topicId = topicId;
        }
    }
}
