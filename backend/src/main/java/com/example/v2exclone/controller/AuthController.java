package com.example.v2exclone.controller;

import com.example.v2exclone.common.ApiResponse;
import com.example.v2exclone.dto.UserDTO;
import com.example.v2exclone.entity.User;
import com.example.v2exclone.exception.BusinessException;
import com.example.v2exclone.service.UserService;
import com.example.v2exclone.service.TokenBlacklistService;
import com.example.v2exclone.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @GetMapping("/user")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails,
                                            @AuthenticationPrincipal OAuth2User oAuth2User) {
        User user = null;

        // 如果是JWT认证的用户
        if (userDetails != null && oAuth2User == null) {
            Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
            if (userOptional.isPresent()) {
                user = userOptional.get();
            }
        }
        // 如果是OAuth2认证的用户
        else if (oAuth2User != null) {
            user = userService.processOAuth2User("github", oAuth2User.getAttributes());
        }

        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", false);
            return response;
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        UserDTO userDTO = new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getAvatar(),
            user.getBio(),
            user.getCreatedAt()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("user", userDTO);
        response.put("token", token);

        return response;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(@AuthenticationPrincipal UserDetails userDetails,
                                    HttpServletRequest request) {
        // 获取当前用户信息（如果有的话）
        String username = null;
        if (userDetails != null) {
            username = userDetails.getUsername();
            log.info("用户 {} 正在退出登录", username);
        }

        // 获取Authorization header中的token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("将用户 {} 的token加入黑名单", username);
            // 将token加入Redis黑名单
            tokenBlacklistService.blacklistToken(token, username);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "退出登录成功");
        response.put("timestamp", System.currentTimeMillis());
        if (username != null) {
            response.put("user", username);
        }

        return response;
    }

    @GetMapping("/blacklist/status")
    public ResponseEntity<?> getBlacklistStatus() {
        try {
            long blacklistSize = tokenBlacklistService.getBlacklistSize();

            Map<String, Object> response = new HashMap<>();
            response.put("blacklistSize", blacklistSize);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to get blacklist status: " + e.getMessage()));
        }
    }

    @PostMapping("/logout/all")
    public ResponseEntity<?> logoutAllDevices(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not authenticated"));
            }

            String username = userDetails.getUsername();
            tokenBlacklistService.blacklistAllUserTokens(username);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "All devices logged out successfully");
            response.put("user", username);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Logout all failed: " + e.getMessage()));
        }
    }
}
