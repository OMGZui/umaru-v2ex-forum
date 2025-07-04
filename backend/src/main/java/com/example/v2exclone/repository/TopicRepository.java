package com.example.v2exclone.repository;

import com.example.v2exclone.entity.Topic;
import com.example.v2exclone.entity.Node;
import com.example.v2exclone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    
    Page<Topic> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    Page<Topic> findByNodeOrderByCreatedAtDesc(Node node, Pageable pageable);
    
    Page<Topic> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
    
    @Query("SELECT t FROM Topic t ORDER BY t.replyCount DESC, t.createdAt DESC")
    Page<Topic> findHotTopics(Pageable pageable);
    
    List<Topic> findTop10ByOrderByCreatedAtDesc();
}
