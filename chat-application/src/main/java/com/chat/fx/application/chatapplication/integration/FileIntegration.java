package com.chat.fx.application.chatapplication.integration;

import com.chat.fx.application.chatapplication.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component
public class FileIntegration {
    private static final Logger logger = LoggerFactory.getLogger(FileIntegration.class);

    private HttpUtils httpUtils = new HttpUtils();

    public void uploadScreenShot(File file)
            throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String apiUrl = "http://localhost:9002/api/v1/files";
        httpUtils.callHttpPostWithFileUpload(apiUrl, file);
        if(file.delete()) {
            logger.debug("Screen shot is deleted.");
        }
    }

    public boolean downloadLatestImage(int userId, String userName)
            throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String downloadUrl = "http://localhost:9002/api/v1/files/" + userId + "/download/latest?user-name=" +userName;
        return httpUtils.callHttpGetDownloadFile(downloadUrl);
    }
}
