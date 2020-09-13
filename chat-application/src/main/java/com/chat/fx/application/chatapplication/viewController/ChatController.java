package com.chat.fx.application.chatapplication.viewController;

import com.chat.fx.application.chatapplication.config.BearerContext;
import com.chat.fx.application.chatapplication.config.MyStompSessionHandler;
import com.chat.fx.application.chatapplication.domain.ChatMessageDto;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatController {
    private StompSession stompSession;

    private static String URL = "ws://localhost:8089/chat";

    private static Logger logger = LogManager.getLogger(ChatController.class);

    private final VBox chatBox = new VBox(15);

    private List<Label> messages = new ArrayList<>();

    private int index = 0;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextArea inputContent;

    @FXML
    public void createConnectWebSocket() {
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        SockJsClient sockjsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockjsClient);

        // StringMessageConverter
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new MyStompSessionHandler(this);
        ListenableFuture<StompSession> sessionListenableFuture = stompClient.connect(URL, sessionHandler);

        try {
            stompSession = sessionListenableFuture.completable().get();
            logger.info("New session established : " + stompSession.getSessionId());
            ChatMessageDto chatMessageDto = new ChatMessageDto();
            chatMessageDto.setSender(BearerContext.getUserName());
            chatMessageDto.setType(ChatMessageDto.MessageType.JOIN);
            stompSession.send("/app/chat.addUser", chatMessageDto);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sendMessage() {
        String content = inputContent.getText();
        inputContent.clear();

        //set object chat
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setContent(content);
        chatMessageDto.setSender(BearerContext.getUserName());
        chatMessageDto.setType(ChatMessageDto.MessageType.CHAT);

        stompSession.send("/app/chat.sendMessage", chatMessageDto);
    }

    public void addNewMessage(ChatMessageDto chatMessageDto) {
        Platform.runLater(() -> {
            Label label = new Label(chatMessageDto.getSender().toUpperCase() + ": " +chatMessageDto.getContent() + " " + LocalTime.now());
            label.setFont(new Font("Tahoma", 16));
            if (chatMessageDto.getSender().equalsIgnoreCase(BearerContext.getUserName())) {
                label.setTextFill((Color.web("#4287f5")));
            }

            messages.add(label);
            scrollPane.setContent(chatBox);
            chatBox.getChildren().add(messages.get(index));
            index++;
        });
    }
}
