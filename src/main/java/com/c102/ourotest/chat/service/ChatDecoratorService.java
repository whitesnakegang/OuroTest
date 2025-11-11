package com.c102.ourotest.chat.service;

import com.c102.ourotest.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatDecoratorService implements ChatService {

    private final ChatSessionService chatSessionService;

    @Override
    public ChatMessage handleInboundMessage(String roomId, ChatMessage message) {
        ChatMessage withDefaults = message.withDefaults(roomId, ChatMessage.MessageType.TALK);
        ChatMessage normalized = withDefaults.normalize();
        chatSessionService.track(roomId, normalized);
        return normalized;
    }

    @Override
    public ChatMessage enrichForBroadcast(ChatMessage message) {
        ChatMessage decorated = chatSessionService.decorate(message);
        return chatSessionService.audit(decorated);
    }
}

