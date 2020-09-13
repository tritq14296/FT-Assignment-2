package com.chat.fx.application.chatapplication.config;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class StageReadyEvent extends ApplicationEvent {
    public Stage getStage() {
        return Stage.class.cast(getSource());
    }

    public StageReadyEvent(Object source) {
        super(source);
    }
}
