package com.example.v2exclone.service;

import com.example.v2exclone.dto.TopicDTO;
import com.example.v2exclone.dto.UserDTO;
import com.example.v2exclone.dto.NodeDTO;
import com.example.v2exclone.entity.Topic;
import com.example.v2exclone.entity.User;
import com.example.v2exclone.entity.Node;
import com.example.v2exclone.repository.TopicRepository;
import com.example.v2exclone.repository.UserRepository;
import com.example.v2exclone.repository.NodeRepository;
import com.example.v2exclone.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TopicService {
    
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private ReplyRepository replyRepository;
    
    public Page<TopicDTO> getAllTopics(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Topic> topics = topicRepository.findAllByOrderByCreatedAtDesc(pageable);
        return topics.map(this::convertToDTO);
    }
    
    public List<TopicDTO> getRecentTopics() {
        List<Topic> topics = topicRepository.findTop10ByOrderByCreatedAtDesc();
        return topics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    public Optional<TopicDTO> getTopicById(Long id) {
        Optional<Topic> topic = topicRepository.findById(id);
        if (topic.isPresent()) {
            Topic t = topic.get();
            t.setViewCount(t.getViewCount() + 1);
            topicRepository.save(t);
            return Optional.of(convertToDTO(t));
        }
        return Optional.empty();
    }
    
    public TopicDTO createTopic(String title, String content, Long authorId, Long nodeId) {
        Optional<User> author = userRepository.findById(authorId);
        Optional<Node> node = nodeRepository.findById(nodeId);

        if (author.isPresent() && node.isPresent()) {
            Topic topic = Topic.builder()
                    .title(title)
                    .content(content)
                    .author(author.get())
                    .node(node.get())
                    .build();
            Topic savedTopic = topicRepository.save(topic);
            return convertToDTO(savedTopic);
        }
        throw new RuntimeException("Author or Node not found");
    }

    public TopicDTO updateTopic(Long topicId, String title, String content, Long nodeId) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        Optional<Node> nodeOptional = nodeRepository.findById(nodeId);

        if (topicOptional.isPresent() && nodeOptional.isPresent()) {
            Topic topic = topicOptional.get();
            topic.setTitle(title);
            topic.setContent(content);
            topic.setNode(nodeOptional.get());
            topic.setUpdatedAt(java.time.LocalDateTime.now());

            Topic savedTopic = topicRepository.save(topic);
            return convertToDTO(savedTopic);
        }
        throw new RuntimeException("Topic or Node not found");
    }

    public void incrementReplyCount(Long topicId) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        if (topicOptional.isPresent()) {
            Topic topic = topicOptional.get();
            topic.setReplyCount(topic.getReplyCount() + 1);
            topicRepository.save(topic);
        }
    }
    
    private TopicDTO convertToDTO(Topic topic) {
        UserDTO authorDTO = new UserDTO(
            topic.getAuthor().getId(),
            topic.getAuthor().getUsername(),
            topic.getAuthor().getEmail(),
            topic.getAuthor().getAvatar(),
            topic.getAuthor().getBio(),
            topic.getAuthor().getCreatedAt()
        );

        NodeDTO nodeDTO = new NodeDTO(
            topic.getNode().getId(),
            topic.getNode().getName(),
            topic.getNode().getSlug(),
            topic.getNode().getDescription(),
            topic.getNode().getCreatedAt()
        );

        // 获取真实的回复数量
        long actualReplyCount = replyRepository.countByTopic(topic);

        return new TopicDTO(
            topic.getId(),
            topic.getTitle(),
            topic.getContent(),
            topic.getCreatedAt(),
            topic.getUpdatedAt(),
            topic.getViewCount(),
            (int) actualReplyCount,  // 使用真实的回复数
            authorDTO,
            nodeDTO
        );
    }
}
