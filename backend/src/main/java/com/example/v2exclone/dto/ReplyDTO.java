package com.example.v2exclone.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserDTO author;
    private Long topicId;
}
