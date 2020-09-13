package com.chatservice.websocket;

import com.chatservice.domain.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketCOnnectListener(SessionConnectEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent disconnectEvent) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());

        String userName = (String) stompHeaderAccessor.getSessionAttributes().get("username");

        if (userName != null) {
            logger.info("User "+ userName + " disconnected.");

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessageType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(userName);

            messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }
}
