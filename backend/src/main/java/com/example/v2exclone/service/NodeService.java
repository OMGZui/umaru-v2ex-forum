package com.example.v2exclone.service;

import com.example.v2exclone.dto.NodeDTO;
import com.example.v2exclone.entity.Node;
import com.example.v2exclone.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NodeService {

    @Autowired
    private NodeRepository nodeRepository;

    public List<NodeDTO> getAllNodes() {
        return nodeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<NodeDTO> getNodeById(Long id) {
        return nodeRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<NodeDTO> getNodeBySlug(String slug) {
        return nodeRepository.findBySlug(slug)
                .map(this::convertToDTO);
    }

    public NodeDTO createNode(String name, String slug, String description) {
        Node node = new Node(name, slug, description);
        Node savedNode = nodeRepository.save(node);
        return convertToDTO(savedNode);
    }

    private NodeDTO convertToDTO(Node node) {
        return new NodeDTO(
                node.getId(),
                node.getName(),
                node.getSlug(),
                node.getDescription(),
                node.getCreatedAt()
        );
    }
}
