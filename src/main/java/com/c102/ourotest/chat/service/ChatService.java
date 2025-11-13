package com.c102.ourotest.chat.service;

import com.c102.ourotest.chat.dto.ChatMessage;

public interface ChatService {

    ChatMessage handleInboundMessage(String roomId, ChatMessage message);

    ChatMessage enrichForBroadcast(ChatMessage message);
}

