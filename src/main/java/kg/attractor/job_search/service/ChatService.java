package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ChatMessageDto;
import kg.attractor.job_search.model.ChatMessage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatService {
    @Transactional
    ChatMessage saveMessage(ChatMessageDto messageDto);

    @Transactional(readOnly = true)
    List<ChatMessage> getConversation(Integer userId1, Integer userId2);

    String getConversationId(Integer userId1, Integer userId2);
}
