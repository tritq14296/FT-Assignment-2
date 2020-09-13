package com.chat.fx.application.chatapplication.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {
    private final String applicationTitle;
    private final Resource fmxlResource;
    private final ApplicationContext applicationContext;

    public StageListener(@Value("${spring.application.ui.title}") String applicationTitle,
                         @Value("classpath:view/loginView.fxml") Resource resource, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        fmxlResource = resource;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        try {
            Stage stage = stageReadyEvent.getStage();
            URL url = this.fmxlResource.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.setTitle(applicationTitle);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
