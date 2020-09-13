package com.chat.fx.application.chatapplication.config;

import com.chat.fx.application.chatapplication.domain.ChatMessageDto;
import com.chat.fx.application.chatapplication.viewController.ChatController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);
    private ChatController chatController;

    public MyStompSessionHandler(ChatController chatController) {
        this.chatController = chatController;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/public", this);
        logger.info("Subscribed to /topic/public");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessageDto.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ChatMessageDto msg = (ChatMessageDto) payload;
        logger.info("Received : " + msg.getContent() + " from : " + msg.getSender());
        if (msg.getContent() != null) {
            chatController.addNewMessage(msg);
        }
    }

    /**
     * A sample message instance.
     * @return instance of <code>Message</code>
     */
}
