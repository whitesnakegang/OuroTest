package com.c102.ourotest.chat.service;

import com.c102.ourotest.chat.dto.ChatMessage;
import com.c102.ourotest.chat.dto.ChatMessage.MessageType;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class ChatSessionService {

    private final ConcurrentMap<String, Instant> roomLastSeen = new ConcurrentHashMap<>();

    public void track(String roomId, ChatMessage message) {
        roomLastSeen.put(roomId, message.getSentAt());
    }

    public ChatMessage decorate(ChatMessage message) {
        Instant lastSeen = roomLastSeen.get(message.getRoomId());
        if (lastSeen == null || message.getSentAt().isAfter(lastSeen)) {
            return message;
        }
        return ChatMessage.builder()
                .roomId(message.getRoomId())
                .sender(message.getSender())
                .content(message.getContent())
                .type(message.getType())
                .sentAt(lastSeen.plusMillis(1))
                .build();
    }

    public ChatMessage audit(ChatMessage message) {
        if (message.getType() == MessageType.ENTER) {
            return appendAudit(message, "entered");
        }
        if (message.getType() == MessageType.LEAVE) {
            return appendAudit(message, "left");
        }
        return message;
    }

    private ChatMessage appendAudit(ChatMessage message, String action) {
        String decoratedContent = message.getContent() + " (system:" + action + ")";
        return ChatMessage.builder()
                .roomId(message.getRoomId())
                .sender(message.getSender())
                .content(decoratedContent)
                .type(message.getType())
                .sentAt(message.getSentAt())
                .build();
    }
}

