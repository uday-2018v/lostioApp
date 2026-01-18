package com.lostio.repository;

import com.lostio.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAt(
            String senderId1, String receiverId1, String senderId2, String receiverId2);
}
