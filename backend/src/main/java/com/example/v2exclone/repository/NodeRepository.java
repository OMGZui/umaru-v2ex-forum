package com.example.v2exclone.repository;

import com.example.v2exclone.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    
    Optional<Node> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
    
    boolean existsByName(String name);
}
