package com.example.v2exclone.repository;

import com.example.v2exclone.entity.AuthProvider;
import com.example.v2exclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);

    Optional<User> findByGithubId(String githubId);

    /**
     * 查询最近活跃的用户
     * 基于最近的主题和回复活动计算活跃度
     * @param sinceDate 开始时间
     * @return 活跃用户列表
     */
    @Query("""
        SELECT u FROM User u
        WHERE u.id IN (
            SELECT DISTINCT u2.id FROM User u2
            LEFT JOIN u2.topics t
            LEFT JOIN u2.replies r
            WHERE (t.createdAt >= :sinceDate OR r.createdAt >= :sinceDate)
        )
        ORDER BY (
            COALESCE((SELECT COUNT(t2) FROM Topic t2 WHERE t2.author = u AND t2.createdAt >= :sinceDate), 0) * 3 +
            COALESCE((SELECT COUNT(r2) FROM Reply r2 WHERE r2.author = u AND r2.createdAt >= :sinceDate), 0)
        ) DESC
        """)
    List<User> findMostActiveUsers(@Param("sinceDate") LocalDateTime sinceDate);

    /**
     * 查询用户在指定时间段内的主题数量
     * @param userId 用户ID
     * @param sinceDate 开始时间
     * @return 主题数量
     */
    @Query("SELECT COUNT(t) FROM Topic t WHERE t.author.id = :userId AND t.createdAt >= :sinceDate")
    long countTopicsByUserSince(@Param("userId") Long userId, @Param("sinceDate") LocalDateTime sinceDate);

    /**
     * 查询用户在指定时间段内的回复数量
     * @param userId 用户ID
     * @param sinceDate 开始时间
     * @return 回复数量
     */
    @Query("SELECT COUNT(r) FROM Reply r WHERE r.author.id = :userId AND r.createdAt >= :sinceDate")
    long countRepliesByUserSince(@Param("userId") Long userId, @Param("sinceDate") LocalDateTime sinceDate);
}
