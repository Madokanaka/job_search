package kg.attractor.job_search.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private LocalDateTime timestamp;
}