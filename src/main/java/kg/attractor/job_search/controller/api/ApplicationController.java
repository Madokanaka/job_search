package kg.attractor.job_search.controller.api;

import jakarta.validation.Valid;
import kg.attractor.job_search.dto.RespondenApplicantDto;
import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.model.ChatRoom;
import kg.attractor.job_search.service.ApplicationService;
import kg.attractor.job_search.service.ChatService;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final MessageSource messageSource;
    private final UserService userService;
    private final ChatService chatService;

    @PostMapping("/{vacancyId}/respond")
    public ResponseEntity<?> respondToVacancy(
            @PathVariable Integer vacancyId,
            @RequestParam Integer resumeId) {
        RespondenApplicantDto response = applicationService.respondToVacancy(resumeId, vacancyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/respond")
    public ResponseEntity<Map<String, Object>> applyToVacancy(
            @Valid @RequestBody Map<String, Integer> request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        if (principal == null) {
            throw new BadRequestException(messageSource.getMessage("error.user.not.authenticated", null, LocaleContextHolder.getLocale()));
        }

        Integer resumeId = request.get("resumeId");
        Integer vacancyId = request.get("vacancyId");
        Integer employerId = request.get("employerId");

        if (resumeId == null || vacancyId == null || employerId == null) {
            throw new BadRequestException(messageSource.getMessage("error.invalid.request", null, LocaleContextHolder.getLocale()));
        }

        UserDto user = userService.findUserByEmail(principal.getUsername())
                .orElseThrow(() -> new BadRequestException(
                        messageSource.getMessage("error.user.not.found", new Object[]{principal.getUsername()}, LocaleContextHolder.getLocale())));

        if (!user.getAccountType().equalsIgnoreCase("applicant")) {
            throw new BadRequestException(messageSource.getMessage("error.user.not.applicant", null, LocaleContextHolder.getLocale()));
        }

        RespondenApplicantDto application = applicationService.respondToVacancy(resumeId, vacancyId);

        ChatRoom chatRoom = chatService.getOrCreateChatRoom(user.getId(), employerId);

        Map<String, Object> response = new HashMap<>();
        response.put("chatRoomId", chatRoom.getId());
        response.put("message", messageSource.getMessage("vacancies.apply.success", null, LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(response);
    }
}
