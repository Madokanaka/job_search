package kg.attractor.job_search.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRoomDto {
    private Long id;
    private Integer user1Id;
    private Integer user2Id;
    private String otherUserEmail;
    private String otherUserName;
    private LocalDateTime createdAt;
}