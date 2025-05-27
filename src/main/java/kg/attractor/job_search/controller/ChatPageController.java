package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.ChatMessageDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.model.ChatRoom;
import kg.attractor.job_search.service.ChatService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatPageController {

    private final UserService userService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/chat/{chatRoomId:[0-9]+}")
    public String chatPage(@PathVariable Long chatRoomId, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model) {
        UserDto candidate = getCandidate(principal);
        ChatRoom chatRoom = chatService.findById(chatRoomId);

        if (!chatRoom.getUser1().getId().equals(candidate.getId()) && !chatRoom.getUser2().getId().equals(candidate.getId())) {
            throw new BadRequestException("You are not a participant of this chat room");
        }

        UserDto otherUser = userService.getUserById(chatRoom.getUser1().getId().equals(candidate.getId()) ? chatRoom.getUser2().getId() : chatRoom.getUser1().getId()).get();

        if (otherUser.getAccountType().equalsIgnoreCase(candidate.getAccountType())) {
            throw new BadRequestException("You cannot chat with other " + otherUser.getAccountType());
        }

        model.addAttribute("employer", otherUser);
        model.addAttribute("messages", chatService.getMessagesByChatRoom(chatRoomId));
        model.addAttribute("candidate", candidate);
        model.addAttribute("chatRoomId", chatRoomId);
        return "chat";
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDto messageDto) {
        chatService.saveMessage(messageDto);
        String destination = "/topic/chat/" + chatService.getChatRoomId(messageDto.getChatRoomId());
        messagingTemplate.convertAndSend(destination, messageDto);
    }

    private UserDto getCandidate(org.springframework.security.core.userdetails.User authentication) {
        return userService.findUserByEmail(authentication.getUsername()).get();
    }

    @GetMapping("/chat/start/{otherUserId:[0-9]+}")
    public String startChat(@PathVariable Integer otherUserId, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        UserDto candidate = getCandidate(principal);
        if (candidate.getId().equals(otherUserId)) {
            throw new BadRequestException("Cannot start chat with yourself");
        }
        UserDto otherUser = userService.getUserById(otherUserId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        if (otherUser.getAccountType().equalsIgnoreCase(candidate.getAccountType())) {
            throw new BadRequestException("You cannot chat with other " + otherUser.getAccountType());
        }
        ChatRoom chatRoom = chatService.getOrCreateChatRoom(candidate.getId(), otherUserId);
        return "redirect:/chat/" + chatRoom.getId();
    }
}