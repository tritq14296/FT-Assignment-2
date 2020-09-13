package com.chat.fx.application.chatapplication.integration;

import com.chat.fx.application.chatapplication.domain.AuthenticateResponseDto;
import com.chat.fx.application.chatapplication.domain.UserLoginDto;
import com.chat.fx.application.chatapplication.utils.HttpUtils;
import com.chat.fx.application.chatapplication.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component
public class AuthenticationIntegration {
    private static final String loginUrl = "http://localhost:9002/api/v1/login";
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationIntegration.class);

    @Autowired
    private HttpUtils httpUtils;

    public AuthenticateResponseDto authenticateUser(UserLoginDto userLoginDto) {
        AuthenticateResponseDto authenticateResponseDto = new AuthenticateResponseDto();
        try {
            ResponseEntity responseEntity = httpUtils.callHttpPost(loginUrl, userLoginDto);

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                logger.debug("Authentication Failure:" +responseEntity.getBody());
            } else {
                authenticateResponseDto = (AuthenticateResponseDto)
                        JsonUtils.getObject(responseEntity.getBody().toString(), AuthenticateResponseDto.class);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }

        return authenticateResponseDto;
    }
}
