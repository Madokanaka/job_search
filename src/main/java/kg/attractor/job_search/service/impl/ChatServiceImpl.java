package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ChatMessageDto;
import kg.attractor.job_search.dto.ChatRoomDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.exception.ResourceNotFoundException;
import kg.attractor.job_search.model.ChatMessage;
import kg.attractor.job_search.model.ChatRoom;
import kg.attractor.job_search.repository.ChatMessageRepository;
import kg.attractor.job_search.repository.ChatRoomRepository;
import kg.attractor.job_search.service.ChatService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    @Transactional
    @Override
    public ChatMessage saveMessage(ChatMessageDto messageDto) {
        ChatMessage message = new ChatMessage();
        message.setChatRoom(chatRoomRepository.findById(messageDto.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found")));
        message.setSender(userService.getUserModelById(messageDto.getSenderId()));
        message.setContent(messageDto.getContent());
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatMessage> getMessagesByChatRoom(Long chatRoomId) {
        return messageRepository.findByChatRoomIdOrderByTimestampAsc(chatRoomId);
    }

    @Transactional
    @Override
    public ChatRoom getOrCreateChatRoom(Integer userId1, Integer userId2) {
        return chatRoomRepository.findByUser1IdAndUser2IdOrUser2IdAndUser1Id(userId1, userId2)
                .orElseGet(() -> {
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setUser1(userService.getUserModelById(userId1));
                    chatRoom.setUser2(userService.getUserModelById(userId2));
                    return chatRoomRepository.save(chatRoom);
                });
    }

    @Override
    public String getChatRoomId(Long chatRoomId) {
        return String.valueOf(chatRoomId);
    }

    @Transactional(readOnly = true)
    @Override
    public ChatRoomDto findById(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found"));
        return toDto(chatRoom);
    }

    private ChatRoomDto toDto(ChatRoom chatRoom) {
        ChatRoomDto dto = new ChatRoomDto();
        dto.setId(chatRoom.getId());
        dto.setUser1Id(chatRoom.getUser1().getId());
        dto.setUser2Id(chatRoom.getUser2().getId());
        dto.setCreatedAt(chatRoom.getCreatedAt());
        return dto;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isChatParticipant(Long chatRoomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat room not found"));
        UserDto user = userService.findUserByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return chatRoom.getUser1().getId().equals(user.getId()) || chatRoom.getUser2().getId().equals(user.getId());
    }
}