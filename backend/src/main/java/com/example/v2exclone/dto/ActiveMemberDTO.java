package com.example.v2exclone.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 活跃会员数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveMemberDTO {
    
    private Long id;                    // 用户ID
    private String username;            // 用户名
    private String avatar;              // 头像URL
    private String bio;                 // 个人简介
    private LocalDateTime lastActiveAt; // 最后活跃时间
    private Integer activityScore;      // 活跃度分数
    private Integer topicCount;         // 主题数量
    private Integer replyCount;         // 回复数量
}
