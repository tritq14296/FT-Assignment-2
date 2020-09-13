package com.chat.fx.application.chatapplication;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaFxApplication {
    public static void main(String[] args) {
        Application.launch(ChatApplication.class, args);
    }
}
