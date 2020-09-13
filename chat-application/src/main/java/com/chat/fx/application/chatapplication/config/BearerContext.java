package com.chat.fx.application.chatapplication.config;

public class BearerContext {
    private static String bearerToken;
    private static String userName;

    public static String getBearerToken() {
        return bearerToken;
    }

    public static void setBearerToken(String bearerToken) {
        BearerContext.bearerToken = bearerToken;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        BearerContext.userName = userName;
    }
}
