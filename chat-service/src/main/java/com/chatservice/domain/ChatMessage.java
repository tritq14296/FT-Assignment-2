package com.chatservice.domain;

import java.time.LocalTime;
import java.util.Calendar;

public class ChatMessage {
    private String content;

    private String sender;

    private LocalTime timeSend;

    private MessageType messageType;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalTime getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(LocalTime timeSend) {
        this.timeSend = timeSend;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
