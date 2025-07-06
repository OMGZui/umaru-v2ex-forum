package com.example.v2exclone.controller;

import com.example.v2exclone.dto.NodeDTO;
import com.example.v2exclone.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nodes")
@CrossOrigin(origins = "http://localhost:3000")
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @GetMapping
    public ResponseEntity<List<NodeDTO>> getAllNodes() {
        List<NodeDTO> nodes = nodeService.getAllNodes();
        return ResponseEntity.ok(nodes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NodeDTO> getNodeById(@PathVariable Long id) {
        Optional<NodeDTO> node = nodeService.getNodeById(id);
        return node.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<NodeDTO> getNodeBySlug(@PathVariable String slug) {
        Optional<NodeDTO> node = nodeService.getNodeBySlug(slug);
        return node.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NodeDTO> createNode(@RequestBody CreateNodeRequest request) {
        try {
            NodeDTO node = nodeService.createNode(
                request.getName(),
                request.getSlug(),
                request.getDescription()
            );
            return ResponseEntity.ok(node);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Inner class for request body
    public static class CreateNodeRequest {
        private String name;
        private String slug;
        private String description;

        // Getters and Setters
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
    }
}
