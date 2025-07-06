package com.example.v2exclone.service;

import com.example.v2exclone.dto.ActiveMemberDTO;
import com.example.v2exclone.entity.User;
import com.example.v2exclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活跃会员服务
 */
@Service
@Transactional(readOnly = true)
public class ActiveMemberService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 获取最近活跃会员列表
     * @param days 最近几天内的活动
     * @param limit 返回数量限制
     * @return 活跃会员列表
     */
    public List<ActiveMemberDTO> getRecentActiveMembers(int days, int limit) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
        
        // 获取最近活跃的用户
        List<User> activeUsers = userRepository.findMostActiveUsers(sinceDate);
        
        // 转换为DTO并计算活跃度分数
        return activeUsers.stream()
                .limit(limit)
                .map(user -> convertToActiveMemberDTO(user, sinceDate))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取默认的最近活跃会员（最近7天，最多6个）
     * @return 活跃会员列表
     */
    public List<ActiveMemberDTO> getRecentActiveMembers() {
        return getRecentActiveMembers(7, 6);
    }
    
    /**
     * 将User实体转换为ActiveMemberDTO
     * @param user 用户实体
     * @param sinceDate 统计开始时间
     * @return ActiveMemberDTO
     */
    private ActiveMemberDTO convertToActiveMemberDTO(User user, LocalDateTime sinceDate) {
        // 计算用户在指定时间段内的活动数据
        long topicCount = userRepository.countTopicsByUserSince(user.getId(), sinceDate);
        long replyCount = userRepository.countRepliesByUserSince(user.getId(), sinceDate);
        
        // 计算活跃度分数：主题权重3，回复权重1
        int activityScore = (int) (topicCount * 3 + replyCount);
        
        // 确定最后活跃时间（取最近的主题或回复时间）
        LocalDateTime lastActiveAt = getLastActiveTime(user, sinceDate);
        
        return new ActiveMemberDTO(
            user.getId(),
            user.getUsername(),
            user.getAvatar(),
            user.getBio(),
            lastActiveAt,
            activityScore,
            (int) topicCount,
            (int) replyCount
        );
    }
    
    /**
     * 获取用户最后活跃时间
     * @param user 用户
     * @param sinceDate 统计开始时间
     * @return 最后活跃时间
     */
    private LocalDateTime getLastActiveTime(User user, LocalDateTime sinceDate) {
        LocalDateTime lastTopicTime = null;
        LocalDateTime lastReplyTime = null;
        
        // 获取最近的主题时间
        if (user.getTopics() != null && !user.getTopics().isEmpty()) {
            lastTopicTime = user.getTopics().stream()
                    .filter(topic -> topic.getCreatedAt().isAfter(sinceDate))
                    .map(topic -> topic.getCreatedAt())
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
        }
        
        // 获取最近的回复时间
        if (user.getReplies() != null && !user.getReplies().isEmpty()) {
            lastReplyTime = user.getReplies().stream()
                    .filter(reply -> reply.getCreatedAt().isAfter(sinceDate))
                    .map(reply -> reply.getCreatedAt())
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
        }
        
        // 返回最近的时间
        if (lastTopicTime != null && lastReplyTime != null) {
            return lastTopicTime.isAfter(lastReplyTime) ? lastTopicTime : lastReplyTime;
        } else if (lastTopicTime != null) {
            return lastTopicTime;
        } else if (lastReplyTime != null) {
            return lastReplyTime;
        } else {
            return user.getUpdatedAt(); // 如果没有活动，返回用户更新时间
        }
    }
}
