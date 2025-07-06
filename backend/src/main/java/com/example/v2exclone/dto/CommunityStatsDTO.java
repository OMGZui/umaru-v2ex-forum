package com.example.v2exclone.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 社区统计数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityStatsDTO {
    
    private long totalMembers;      // 注册会员总数
    private long totalTopics;       // 主题总数
    private long totalReplies;      // 回复总数
    private long todayTopics;       // 今日主题数
    private long todayReplies;      // 今日回复数
}
