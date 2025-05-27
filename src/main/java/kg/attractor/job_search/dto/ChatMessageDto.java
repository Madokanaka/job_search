package kg.attractor.job_search.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private Long chatRoomId;
    private Integer senderId;
    private String content;
    private LocalDateTime timestamp;
}