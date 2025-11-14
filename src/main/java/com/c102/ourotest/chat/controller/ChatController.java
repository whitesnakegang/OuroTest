package com.c102.ourotest.chat.controller;

import com.c102.ourotest.chat.dto.ChatMessage;
import com.c102.ourotest.chat.service.ChatFacadeService;
import kr.co.ouroboros.core.global.annotation.ApiState;
import kr.co.ouroboros.core.global.annotation.ApiState.State;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * STOMP 테스트 가이드
 * <p>
 * 1. SockJS/STOMP로 {@code /ws}에 연결합니다.<br>
 * 2. {@code /topic/chat/{roomId}}를 구독합니다.<br>
 * 3. {@code /app/chat/{roomId}}로 메시지를 보내거나,
 *    REST {@code POST /api/chat/{roomId}/send} 호출로 동일한 메시지를 받을 수 있습니다.
 */
@Controller
@RequiredArgsConstructor
public class ChatController {

    private static final String CHAT_TOPIC_PREFIX = "/topic/chat/";

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatFacadeService chatFacadeService;

    @MessageMapping("/chat/{roomId}")
    @ApiState(state = State.COMPLETED)
    public void relay(@DestinationVariable String roomId,
                      @Payload ChatMessage message) {
        ChatMessage payload = preparePayload(message, roomId);
        messagingTemplate.convertAndSend(topic(roomId), payload);
    }

    @PostMapping("/api/chat/{roomId}/send")
    @ApiState(state = State.COMPLETED)
    @ResponseBody
    public void sendViaRest(@PathVariable String roomId,
                            @RequestBody ChatMessage message) {
        ChatMessage payload = preparePayload(message, roomId);
        messagingTemplate.convertAndSend(topic(roomId), payload);
    }

    private String topic(String roomId) {
        return CHAT_TOPIC_PREFIX + roomId;
    }

    private ChatMessage preparePayload(ChatMessage message, String roomId) {
        return chatFacadeService.process(roomId, message);
    }
}

