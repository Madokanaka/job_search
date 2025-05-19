package kg.attractor.job_search.controller;

import kg.attractor.job_search.dto.ChatMessageDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.service.ChatService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
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


    @GetMapping("/chat/{conversationId:[0-9]+-[0-9]+}")
    public String chatPage(@PathVariable String conversationId, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, Model model) {
        String[] ids = conversationId.split("-");
        if (ids.length != 2) {
            throw new IllegalArgumentException("Invalid conversation ID");
        }
        int id1 = Integer.parseInt(ids[0]);
        int id2 = Integer.parseInt(ids[1]);

        UserDto candidate = getCandidate(principal);

        int candidateId = candidate.getId();
        int employerId = (candidateId == id1) ? id2 : id1;

        UserDto employer = userService.getUserById(employerId).get();

        model.addAttribute("employer", employer);
        model.addAttribute("messages", chatService.getConversation(candidate.getId(), employerId));
        model.addAttribute("candidate", candidate);
        model.addAttribute("employerId", employerId);
        model.addAttribute("conversationId", conversationId);
        return "chat";
    }

    @MessageMapping("chat.send")
    public void sendMessage(@Payload ChatMessageDto messageDto) {
        chatService.saveMessage(messageDto);

        String destination = "/topic/chat/" + chatService.getConversationId(
                messageDto.getSenderId(), messageDto.getReceiverId());
        messagingTemplate.convertAndSend(destination, messageDto);
    }

    private UserDto getCandidate(org.springframework.security.core.userdetails.User authentication) {
        return userService.findUserByEmail(authentication.getUsername()).get();

    }
}