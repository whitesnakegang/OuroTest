package com.c102.ourotest.chat.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String roomId;
    private String sender;
    private String content;
    private MessageType type;
    @Builder.Default
    private Instant sentAt = Instant.now();

    public ChatMessage withDefaults(String fallbackRoomId, MessageType fallbackType) {
        return ChatMessage.builder()
                .roomId(roomId != null ? roomId : fallbackRoomId)
                .sender(sender != null && !sender.isBlank() ? sender : "system")
                .content(content)
                .type(type != null ? type : fallbackType)
                .sentAt(sentAt != null ? sentAt : Instant.now())
                .build();
    }

    public ChatMessage normalize() {
        MessageType resolvedType = type != null ? type : MessageType.TALK;
        String resolvedContent = content;

        if ((resolvedContent == null || resolvedContent.isBlank()) && sender != null) {
            resolvedContent = switch (resolvedType) {
                case ENTER -> sender + " 님이 입장했습니다.";
                case LEAVE -> sender + " 님이 퇴장했습니다.";
                case TALK -> "";
            };
        }

        return ChatMessage.builder()
                .roomId(roomId)
                .sender(sender)
                .content(resolvedContent)
                .type(resolvedType)
                .sentAt(sentAt != null ? sentAt : Instant.now())
                .build();
    }

    public enum MessageType {
        ENTER,
        TALK,
        LEAVE
    }
}

