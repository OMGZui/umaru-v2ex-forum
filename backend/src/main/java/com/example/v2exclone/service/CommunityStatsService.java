package com.example.v2exclone.service;

import com.example.v2exclone.dto.CommunityStatsDTO;
import com.example.v2exclone.repository.UserRepository;
import com.example.v2exclone.repository.TopicRepository;
import com.example.v2exclone.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 社区统计服务
 */
@Service
@Transactional(readOnly = true)
public class CommunityStatsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TopicRepository topicRepository;
    
    @Autowired
    private ReplyRepository replyRepository;
    
    /**
     * 获取社区统计数据
     * @return 社区统计数据DTO
     */
    public CommunityStatsDTO getCommunityStats() {
        // 获取总数统计
        long totalMembers = userRepository.count();
        long totalTopics = topicRepository.count();
        long totalReplies = replyRepository.count();
        
        // 获取今日统计（从今天00:00:00开始）
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        long todayTopics = topicRepository.countByCreatedAtAfter(todayStart);
        long todayReplies = replyRepository.countByCreatedAtAfter(todayStart);
        
        return new CommunityStatsDTO(
            totalMembers,
            totalTopics,
            totalReplies,
            todayTopics,
            todayReplies
        );
    }
    
    /**
     * 获取注册会员总数
     * @return 注册会员总数
     */
    public long getTotalMembers() {
        return userRepository.count();
    }
    
    /**
     * 获取主题总数
     * @return 主题总数
     */
    public long getTotalTopics() {
        return topicRepository.count();
    }
    
    /**
     * 获取回复总数
     * @return 回复总数
     */
    public long getTotalReplies() {
        return replyRepository.count();
    }
    
    /**
     * 获取今日主题数
     * @return 今日主题数
     */
    public long getTodayTopics() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        return topicRepository.countByCreatedAtAfter(todayStart);
    }
    
    /**
     * 获取今日回复数
     * @return 今日回复数
     */
    public long getTodayReplies() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        return replyRepository.countByCreatedAtAfter(todayStart);
    }
}
