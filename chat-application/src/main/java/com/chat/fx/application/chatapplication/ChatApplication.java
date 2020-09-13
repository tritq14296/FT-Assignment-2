package com.chat.fx.application.chatapplication;

import com.chat.fx.application.chatapplication.config.StageReadyEvent;
import com.chat.fx.application.chatapplication.integration.LogoutIntegration;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class ChatApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void stop() throws Exception {
        LogoutIntegration logoutIntegration = new LogoutIntegration();
        logoutIntegration.userLogout();
        this.applicationContext.close();
        System.exit(0);
    }

    @Override
    public void init() throws Exception {
        ApplicationContextInitializer<GenericApplicationContext> initializer = genericApplicationContext -> {
            genericApplicationContext.registerBean(Application.class, () -> ChatApplication.this);
            genericApplicationContext.registerBean(Parameters.class, this::getParameters);
            genericApplicationContext.registerBean(HostServices.class, this::getHostServices);
        };

        this.applicationContext = new SpringApplicationBuilder().sources(JavaFxApplication.class)
                .initializers(initializer).run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.applicationContext.publishEvent(new StageReadyEvent(primaryStage));
    }
}
