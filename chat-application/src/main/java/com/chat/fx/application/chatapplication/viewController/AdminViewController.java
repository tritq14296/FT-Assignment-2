package com.chat.fx.application.chatapplication.viewController;

import com.chat.fx.application.chatapplication.domain.MemberDto;
import com.chat.fx.application.chatapplication.domain.MembersDto;
import com.chat.fx.application.chatapplication.integration.UserDetailIntegration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdminViewController {

    @FXML
    private Label pageNumber;

    @FXML
    private Button preBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private GridPane gridPane;

    @FXML
    private Parent memberDetailView;

    private UserDetailIntegration userDetailIntegration = new UserDetailIntegration();

    private int total;

    private int pageCurrentNumber;

    private int totalPage;

    public void getDefaultPage() {
        //with start page, preBtn is disable.
        preBtn.setVisible(false);

        MembersDto membersDto = userDetailIntegration.getMemberList(0, 5);
        total = membersDto.getTotal();
        totalPage = total/5 + 1;

        if (pageCurrentNumber + 1 == totalPage) {
            nextBtn.setVisible(false);
        }

        if (membersDto.getMembers() == null) {
            gridPane.add(new Label("No member to display."), 0, 0, 1, 1);
        } else {
            pageCurrentNumber = 0;
            createContentView(membersDto);
        }
    }

    private void createContentView(MembersDto membersDto) {
        pageNumber.setText("Page " + (pageCurrentNumber + 1));
        for (int i = 0; i < membersDto.getMembers().size(); i++) {
            MemberDto memberDto = membersDto.getMembers().get(i);
            final int index = i;
            Button checkCurrentInformationBtn = new Button("Check Current Information");
            checkCurrentInformationBtn.setOnAction(event -> showAction(memberDto.getUserId(), memberDto.getUserName()));
            gridPane.add(new Label(memberDto.getUserName()), 0, index, 1, 1);
            gridPane.add(checkCurrentInformationBtn, 1, index, 1, 1);
        }
    }

    public void handleNextAction() {
        pageCurrentNumber = pageCurrentNumber + 1;
        MembersDto membersDto = userDetailIntegration.getMemberList(pageCurrentNumber, 5);
        if (pageCurrentNumber + 1 == totalPage) {
            nextBtn.setVisible(false);
        }
        preBtn.setVisible(true);
        createContentView(membersDto);
    }

    public void handlePreBtn() {
        pageCurrentNumber = pageCurrentNumber - 1;
        MembersDto membersDto = userDetailIntegration.getMemberList(pageCurrentNumber, 5);
        if (pageCurrentNumber == 0) {
            preBtn.setVisible(false);
        }
        nextBtn.setVisible(true);
        createContentView(membersDto);
    }

    public void showAction(int userId, String userName) {
        try {
            Resource resource = new ClassPathResource("/view/MemberDetailView.fxml");
            FXMLLoader loader = new FXMLLoader(resource.getURL());
            memberDetailView = loader.load();
            MemberDetailViewController memberDetailViewController = loader.getController();
            memberDetailViewController.initMember(userId, userName);
            Stage stage = new Stage();
            stage.setTitle("Member Details");
            stage.setScene(new Scene(memberDetailView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
