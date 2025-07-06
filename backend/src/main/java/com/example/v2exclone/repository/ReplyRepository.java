package com.example.v2exclone.repository;

import com.example.v2exclone.entity.Reply;
import com.example.v2exclone.entity.Topic;
import com.example.v2exclone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    
    List<Reply> findByTopicOrderByCreatedAtAsc(Topic topic);
    
    Page<Reply> findByTopicOrderByCreatedAtAsc(Topic topic, Pageable pageable);
    
    Page<Reply> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
    
    long countByTopic(Topic topic);

    // 统计相关查询
    @Query("SELECT COUNT(r) FROM Reply r WHERE r.createdAt >= :startDate")
    long countByCreatedAtAfter(LocalDateTime startDate);
}
