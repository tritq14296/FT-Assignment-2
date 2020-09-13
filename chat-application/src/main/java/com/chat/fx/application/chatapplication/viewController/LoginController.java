package com.chat.fx.application.chatapplication.viewController;

import com.chat.fx.application.chatapplication.config.BearerContext;
import com.chat.fx.application.chatapplication.constants.UserRoleType;
import com.chat.fx.application.chatapplication.domain.AuthenticateResponseDto;
import com.chat.fx.application.chatapplication.domain.UserDetailDto;
import com.chat.fx.application.chatapplication.domain.UserLoginDto;
import com.chat.fx.application.chatapplication.integration.AuthenticationIntegration;
import com.chat.fx.application.chatapplication.integration.UserDetailIntegration;
import com.chat.fx.application.chatapplication.scheduleTask.ScreenShotScheduleTask;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Timer;

@Component
public class LoginController {
    private final HostServices hostServices;

    @Autowired
    private ScreenShotScheduleTask screenShotScheduleTask;

    @Value("classpath:/view/chatView.fxml")
    private Resource chatResource;

    @Value("classpath:/view/AdminView.fxml")
    private Resource adminResource;

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Parent chatView;

    @FXML
    private Parent adminView;

    @FXML
    private Label loginMessage;

    @Autowired
    private AuthenticationIntegration authenticationIntegration;

    @Autowired
    private UserDetailIntegration userDetailIntegration;

    LoginController(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private void authenticate(ActionEvent actionEvent) {
        String userName = userNameField.getText();
        String password = passwordField.getText();

        //set object to authen
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setUserName(userName);
        userLoginDto.setPassword(password);

        AuthenticateResponseDto authenticateResponseDto = authenticationIntegration.authenticateUser(userLoginDto);
        //set token and username to use for application.
        BearerContext.setBearerToken(authenticateResponseDto.getTokenId());

        //get user detail
        UserDetailDto userDetailDto = userDetailIntegration.getUserDetail();

        if (userDetailDto != null) {
            BearerContext.setUserName(userDetailDto.getUserName());

            if (userDetailDto.getUserRole().equalsIgnoreCase(UserRoleType.MEMBER.toString())) {
                loadChatView();
                Scene chatViewParent = new Scene(chatView);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(chatViewParent);
            } else {
                loadAdminView();
                Scene adminViewParent = new Scene(adminView);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(adminViewParent);
            }
        } else {
            loginMessage.setTextFill(Color.rgb(255, 0, 0));
            loginMessage.setText("Username or Password is incorrect! Please try again.");
        }
    }

    private void loadChatView() {
        try {
            FXMLLoader loader = new FXMLLoader(chatResource.getURL());
            chatView = loader.load();
            ChatController chatController = loader.getController();
            chatController.createConnectWebSocket();
            triggerScreenshotTask();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadAdminView() {
        try {
            FXMLLoader loader = new FXMLLoader(adminResource.getURL());
            adminView = loader.load();
            AdminViewController adminController = loader.getController();
            adminController.getDefaultPage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void triggerScreenshotTask() {
        System.out.println("Trigger Screenshot Task");
        Timer timer = new Timer();
        timer.schedule(screenShotScheduleTask,0,  5 * 6 * 1000);
    }

}
