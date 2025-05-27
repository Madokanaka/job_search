package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.ChatMessageDto;
import kg.attractor.job_search.model.ChatMessage;
import kg.attractor.job_search.model.ChatRoom;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatService {
    @Transactional
    ChatMessage saveMessage(ChatMessageDto messageDto);


    @Transactional(readOnly = true)
    List<ChatMessage> getMessagesByChatRoom(Long chatRoomId);

    @Transactional
    ChatRoom getOrCreateChatRoom(Integer userId1, Integer userId2);

    String getChatRoomId(Long chatRoomId);

    ChatRoom findById(Long chatRoomId);
}
