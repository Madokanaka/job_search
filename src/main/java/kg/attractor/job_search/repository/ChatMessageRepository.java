package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
        Integer senderId, Integer receiverId, Integer receiverId2, Integer senderId2);
}