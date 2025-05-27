package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.ChatMessageDto;
import kg.attractor.job_search.dto.ChatRoomDto;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatPageController {

    private final UserService userService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/chat/{chatRoomId:[0-9]+}")
    public String chatPage(@PathVariable Long chatRoomId, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        UserDto candidate = getCandidate(principal);
        ChatRoomDto chatRoom = chatService.findById(chatRoomId);

        if (!chatRoom.getUser1Id().equals(candidate.getId()) && !chatRoom.getUser2Id().equals(candidate.getId())) {
            throw new BadRequestException("You are not a participant of this chat room");
        }

        Integer otherUserId = chatRoom.getUser1Id().equals(candidate.getId()) ? chatRoom.getUser2Id() : chatRoom.getUser1Id();
        UserDto otherUser = userService.getUserById(otherUserId)
                .orElseThrow(() -> new BadRequestException("Other user not found"));

        if (otherUser.getAccountType().equalsIgnoreCase(candidate.getAccountType())) {
            throw new BadRequestException("You cannot chat with other " + otherUser.getAccountType());
        }

        model.addAttribute("employer", otherUser);
        model.addAttribute("messages", chatService.getMessagesByChatRoom(chatRoomId));
        model.addAttribute("candidate", candidate);
        model.addAttribute("chatRoomId", chatRoomId);
        return "chats/chat";
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
        if (principal == null) {
            return "redirect:/auth/login";
        }
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

    @GetMapping("/chats")
    public String chatsPage(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        UserDto candidate = getCandidate(principal);
        List<ChatRoomDto> chats = chatService.getUserChats(candidate.getId());
        model.addAttribute("chats", chats);
        model.addAttribute("candidate", candidate);
        return "chats/chats";
    }
}