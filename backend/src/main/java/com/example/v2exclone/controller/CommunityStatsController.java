package com.example.v2exclone.controller;

import com.example.v2exclone.dto.ActiveMemberDTO;
import com.example.v2exclone.dto.CommunityStatsDTO;
import com.example.v2exclone.service.ActiveMemberService;
import com.example.v2exclone.service.CommunityStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 社区统计控制器
 */
@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "http://localhost:3000")
public class CommunityStatsController {
    
    @Autowired
    private CommunityStatsService communityStatsService;

    @Autowired
    private ActiveMemberService activeMemberService;
    
    /**
     * 获取社区统计数据
     * @return 社区统计数据
     */
    @GetMapping("/community")
    public ResponseEntity<CommunityStatsDTO> getCommunityStats() {
        try {
            CommunityStatsDTO stats = communityStatsService.getCommunityStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取注册会员总数
     * @return 注册会员总数
     */
    @GetMapping("/members")
    public ResponseEntity<Long> getTotalMembers() {
        try {
            long totalMembers = communityStatsService.getTotalMembers();
            return ResponseEntity.ok(totalMembers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取主题总数
     * @return 主题总数
     */
    @GetMapping("/topics")
    public ResponseEntity<Long> getTotalTopics() {
        try {
            long totalTopics = communityStatsService.getTotalTopics();
            return ResponseEntity.ok(totalTopics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取回复总数
     * @return 回复总数
     */
    @GetMapping("/replies")
    public ResponseEntity<Long> getTotalReplies() {
        try {
            long totalReplies = communityStatsService.getTotalReplies();
            return ResponseEntity.ok(totalReplies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取今日主题数
     * @return 今日主题数
     */
    @GetMapping("/today/topics")
    public ResponseEntity<Long> getTodayTopics() {
        try {
            long todayTopics = communityStatsService.getTodayTopics();
            return ResponseEntity.ok(todayTopics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取今日回复数
     * @return 今日回复数
     */
    @GetMapping("/today/replies")
    public ResponseEntity<Long> getTodayReplies() {
        try {
            long todayReplies = communityStatsService.getTodayReplies();
            return ResponseEntity.ok(todayReplies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取最近活跃会员
     * @return 最近活跃会员列表
     */
    @GetMapping("/active-members")
    public ResponseEntity<List<ActiveMemberDTO>> getActiveMembers() {
        try {
            List<ActiveMemberDTO> activeMembers = activeMemberService.getRecentActiveMembers();
            return ResponseEntity.ok(activeMembers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取最近活跃会员（自定义参数）
     * @param days 最近几天内的活动
     * @param limit 返回数量限制
     * @return 最近活跃会员列表
     */
    @GetMapping("/active-members/custom")
    public ResponseEntity<List<ActiveMemberDTO>> getActiveMembers(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "6") int limit) {
        try {
            List<ActiveMemberDTO> activeMembers = activeMemberService.getRecentActiveMembers(days, limit);
            return ResponseEntity.ok(activeMembers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
