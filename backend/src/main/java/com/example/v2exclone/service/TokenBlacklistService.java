package com.example.v2exclone.service;

import com.example.v2exclone.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    private static final String BLACKLIST_PREFIX = "token:blacklist:";
    private static final String USER_TOKENS_PREFIX = "user:tokens:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 将token加入黑名单
     * @param token JWT token
     * @param username 用户名
     */
    public void blacklistToken(String token, String username) {
        try {
            // 获取token的过期时间
            long expirationTime = jwtUtil.getExpirationFromToken(token).getTime();
            long currentTime = System.currentTimeMillis();
            
            if (expirationTime > currentTime) {
                // 计算剩余有效时间
                long ttl = expirationTime - currentTime;
                
                // 将token加入黑名单，设置过期时间为token的剩余有效时间
                String blacklistKey = BLACKLIST_PREFIX + token;
                redisTemplate.opsForValue().set(blacklistKey, username, ttl, TimeUnit.MILLISECONDS);
                
                // 记录用户的token（用于全局logout等功能）
                String userTokensKey = USER_TOKENS_PREFIX + username;
                redisTemplate.opsForSet().add(userTokensKey, token);
                redisTemplate.expire(userTokensKey, ttl, TimeUnit.MILLISECONDS);
                
                System.out.println("Token blacklisted for user: " + username + ", TTL: " + ttl + "ms");
            }
        } catch (Exception e) {
            System.err.println("Failed to blacklist token: " + e.getMessage());
        }
    }

    /**
     * 检查token是否在黑名单中
     * @param token JWT token
     * @return true if token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        try {
            String blacklistKey = BLACKLIST_PREFIX + token;
            return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey));
        } catch (Exception e) {
            System.err.println("Failed to check token blacklist: " + e.getMessage());
            return false;
        }
    }

    /**
     * 将用户的所有token加入黑名单（全局logout）
     * @param username 用户名
     */
    public void blacklistAllUserTokens(String username) {
        try {
            String userTokensKey = USER_TOKENS_PREFIX + username;
            var tokens = redisTemplate.opsForSet().members(userTokensKey);
            
            if (tokens != null) {
                for (Object tokenObj : tokens) {
                    String token = (String) tokenObj;
                    blacklistToken(token, username);
                }
                
                // 清除用户token集合
                redisTemplate.delete(userTokensKey);
                System.out.println("All tokens blacklisted for user: " + username);
            }
        } catch (Exception e) {
            System.err.println("Failed to blacklist all user tokens: " + e.getMessage());
        }
    }

    /**
     * 清理过期的黑名单记录（Redis会自动处理，这个方法主要用于手动清理）
     */
    public void cleanupExpiredTokens() {
        // Redis的TTL机制会自动清理过期的key，通常不需要手动清理
        // 这个方法可以用于统计或其他管理目的
        System.out.println("Token cleanup completed (handled by Redis TTL)");
    }

    /**
     * 获取黑名单中的token数量（用于监控）
     * @return 黑名单token数量
     */
    public long getBlacklistSize() {
        try {
            var keys = redisTemplate.keys(BLACKLIST_PREFIX + "*");
            return keys != null ? keys.size() : 0;
        } catch (Exception e) {
            System.err.println("Failed to get blacklist size: " + e.getMessage());
            return 0;
        }
    }
}
