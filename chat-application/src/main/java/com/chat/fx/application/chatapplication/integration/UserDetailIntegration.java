package com.chat.fx.application.chatapplication.integration;

import com.chat.fx.application.chatapplication.domain.MembersDto;
import com.chat.fx.application.chatapplication.domain.UserDetailDto;
import com.chat.fx.application.chatapplication.domain.UserLogTimeDto;
import com.chat.fx.application.chatapplication.utils.HttpUtils;
import com.chat.fx.application.chatapplication.utils.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component
public class UserDetailIntegration {

    private HttpUtils httpUtils = new HttpUtils();

    public UserDetailDto getUserDetail() {
        String userDetailUrl = "http://localhost:9002/api/v1/users/details";

        UserDetailDto userDetailDto = null;
        try {
            ResponseEntity<?> userResponse = httpUtils.callHttpGet(userDetailUrl);
            if (userResponse.getStatusCode() == HttpStatus.OK && userResponse.getBody() != null) {
                userDetailDto = (UserDetailDto) JsonUtils
                        .getObject(userResponse.getBody().toString(), UserDetailDto.class);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }

        return userDetailDto;
    }

    public UserLogTimeDto getUserLogTimeCurrentDay(int userId) {
        String userLogTimeUrl = "http://localhost:9002/api/v1/users/logs/{user-id}";

        UserLogTimeDto userLogTime = null;
        String apiUrl = userLogTimeUrl.replace("{user-id}", String.valueOf(userId));

        try {
            ResponseEntity<?> userResponse = httpUtils.callHttpGet(apiUrl);
            if (userResponse.getStatusCode() == HttpStatus.OK && userResponse.getBody() != null) {
                userLogTime = (UserLogTimeDto) JsonUtils
                        .getObject(userResponse.getBody().toString(), UserLogTimeDto.class);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }

        return userLogTime;
    }

    public MembersDto getMemberList(int pageNumber, int pageSize) {
        String membersUrl = "http://localhost:9002/api/v1/users/members?page=" + pageNumber + "&size=" + pageSize + "&sort=userId,asc";
        MembersDto membersDto = null;
        try {
            ResponseEntity<?> membersResponse = httpUtils.callHttpGet(membersUrl);
            if (membersResponse.getStatusCode() == HttpStatus.OK && membersResponse.getBody() != null) {
                membersDto = (MembersDto) JsonUtils
                        .getObject(membersResponse.getBody().toString(), MembersDto.class);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }

        return membersDto;
    }
}
