package com.example.v2exclone.service;

import com.example.v2exclone.dto.ReplyDTO;
import com.example.v2exclone.dto.UserDTO;
import com.example.v2exclone.entity.Reply;
import com.example.v2exclone.entity.Topic;
import com.example.v2exclone.entity.User;
import com.example.v2exclone.repository.ReplyRepository;
import com.example.v2exclone.repository.TopicRepository;
import com.example.v2exclone.repository.UserRepository;
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
public class ReplyService {
    
    @Autowired
    private ReplyRepository replyRepository;
    
    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TopicService topicService;
    
    public List<ReplyDTO> getRepliesByTopic(Long topicId) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        if (topicOptional.isPresent()) {
            Topic topic = topicOptional.get();
            List<Reply> replies = replyRepository.findByTopicOrderByCreatedAtAsc(topic);
            return replies.stream().map(this::convertToDTO).collect(Collectors.toList());
        }
        throw new RuntimeException("Topic not found");
    }
    
    public Page<ReplyDTO> getRepliesByTopic(Long topicId, int page, int size) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        if (topicOptional.isPresent()) {
            Topic topic = topicOptional.get();
            Pageable pageable = PageRequest.of(page, size);
            Page<Reply> replies = replyRepository.findByTopicOrderByCreatedAtAsc(topic, pageable);
            return replies.map(this::convertToDTO);
        }
        throw new RuntimeException("Topic not found");
    }
    
    public ReplyDTO createReply(String content, Long authorId, Long topicId) {
        Optional<User> authorOptional = userRepository.findById(authorId);
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        
        if (authorOptional.isPresent() && topicOptional.isPresent()) {
            User author = authorOptional.get();
            Topic topic = topicOptional.get();

            Reply reply = Reply.builder()
                    .content(content)
                    .author(author)
                    .topic(topic)
                    .build();
            Reply savedReply = replyRepository.save(reply);
            
            // 增加主题的回复数
            topicService.incrementReplyCount(topicId);
            
            return convertToDTO(savedReply);
        }
        throw new RuntimeException("Author or Topic not found");
    }
    
    public long getReplyCountByTopic(Long topicId) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        if (topicOptional.isPresent()) {
            Topic topic = topicOptional.get();
            return replyRepository.countByTopic(topic);
        }
        return 0;
    }
    
    public Page<ReplyDTO> getRepliesByAuthor(Long authorId, int page, int size) {
        Optional<User> authorOptional = userRepository.findById(authorId);
        if (authorOptional.isPresent()) {
            User author = authorOptional.get();
            Pageable pageable = PageRequest.of(page, size);
            Page<Reply> replies = replyRepository.findByAuthorOrderByCreatedAtDesc(author, pageable);
            return replies.map(this::convertToDTO);
        }
        throw new RuntimeException("Author not found");
    }
    
    private ReplyDTO convertToDTO(Reply reply) {
        UserDTO authorDTO = new UserDTO(
            reply.getAuthor().getId(),
            reply.getAuthor().getUsername(),
            reply.getAuthor().getEmail(),
            reply.getAuthor().getAvatar(),
            reply.getAuthor().getBio(),
            reply.getAuthor().getCreatedAt()
        );
        
        return new ReplyDTO(
            reply.getId(),
            reply.getContent(),
            reply.getCreatedAt(),
            authorDTO,
            reply.getTopic().getId()
        );
    }
}
