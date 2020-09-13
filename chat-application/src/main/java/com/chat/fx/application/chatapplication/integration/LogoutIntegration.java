package com.chat.fx.application.chatapplication.integration;

import com.chat.fx.application.chatapplication.config.BearerContext;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogoutIntegration {

    private void callHttpGet(String apiUrl, Map<String, String> params) throws IOException {

        HttpGet httpGet = new HttpGet(apiUrl);

        if (params != null) {
            for (Map.Entry<String, String> entry: params.entrySet()) {
                String paramName = entry.getKey();
                String paramValue = entry.getValue();
                httpGet.addHeader(paramName, paramValue);
            }
        }

        HttpClient httpClient = HttpClients.createDefault();
        httpClient.execute(httpGet);
    }

    public void userLogout() {
        String userLogoutUrl = "http://localhost:9002/api/v1/users/logout";
        Map<String, String> userMap = new HashMap<>();
        userMap.put("Authorization", BearerContext.getBearerToken());
        try {
            callHttpGet(userLogoutUrl, userMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
