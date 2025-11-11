package com.c102.ourotest.chat.service;

import com.c102.ourotest.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatFacadeService {

    private final ChatService chatService;

    public ChatMessage process(String roomId, ChatMessage message) {
        ChatMessage handled = chatService.handleInboundMessage(roomId, message);
        return chatService.enrichForBroadcast(handled);
    }
}

