package kg.attractor.job_search.repository;

import kg.attractor.job_search.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.user1.id = :userId1 AND cr.user2.id = :userId2) OR (cr.user1.id = :userId2 AND cr.user2.id = :userId1)")
    Optional<ChatRoom> findByUser1IdAndUser2IdOrUser2IdAndUser1Id(Integer userId1, Integer userId2);
}