package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ChatMessageDto;
import kg.attractor.job_search.model.ChatMessage;
import kg.attractor.job_search.repository.ChatMessageRepository;
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
    private final UserService userService;

    @Transactional
    @Override
    public ChatMessage saveMessage(ChatMessageDto messageDto) {
        ChatMessage message = new ChatMessage();
        message.setSender(userService.getUserModelById(messageDto.getSenderId()));
        message.setReceiver(userService.getUserModelById(messageDto.getReceiverId()));
        message.setContent(messageDto.getContent());
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatMessage> getConversation(Integer userId1, Integer userId2) {
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            userId1, userId2, userId1, userId2);
    }

    @Override
    public String getConversationId(Integer userId1, Integer userId2) {
        return userId1 < userId2 ? userId1 + "-" + userId2 : userId2 + "-" + userId1;
    }
}