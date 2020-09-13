package com.chat.fx.application.chatapplication.viewController;

import com.chat.fx.application.chatapplication.domain.UserLogTimeDto;
import com.chat.fx.application.chatapplication.integration.FileIntegration;
import com.chat.fx.application.chatapplication.integration.UserDetailIntegration;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component
public class MemberDetailViewController {
    @FXML
    private Label loginTime;

    @FXML
    private Label logoutTime;

    @FXML
    private Label userNameLbl;

    @FXML
    private Label errorLog;

    @FXML
    private ImageView screenImg;

    public void initMember(int userId, String userName) {
        FileIntegration fileIntegration = new FileIntegration();
        UserDetailIntegration userDetailIntegration = new UserDetailIntegration();

        UserLogTimeDto userLogTimeDto = userDetailIntegration.getUserLogTimeCurrentDay(userId);
        userNameLbl.setText(userName.toUpperCase());
        if (userLogTimeDto != null) {
            loginTime.setText(userLogTimeDto.getUserLoginTime().toString());
            logoutTime.setText(userLogTimeDto.getUserLogoutTime().toString());
            errorLog.setVisible(false);
            try {
                if (fileIntegration.downloadLatestImage(userId, userName)) {
                    FileInputStream inputStream =
                            new FileInputStream(System.getProperty("user.home") + File.separator + "Capture" + File.separator + "SC.jpg");
                    screenImg.setImage(new Image(inputStream));
                }
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        } else {
            logoutTime.setVisible(false);
            loginTime.setVisible(false);
            errorLog.setText("User does not login in System today!");
        }
    }
}
