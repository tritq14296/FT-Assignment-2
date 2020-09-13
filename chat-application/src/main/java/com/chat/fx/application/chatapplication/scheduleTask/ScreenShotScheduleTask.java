package com.chat.fx.application.chatapplication.scheduleTask;

import com.chat.fx.application.chatapplication.config.FileConfig;
import com.chat.fx.application.chatapplication.constants.CommonConstants;
import com.chat.fx.application.chatapplication.integration.FileIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

@Component
public class ScreenShotScheduleTask extends TimerTask {
    @Autowired
    private FileIntegration fileIntegration;

    static {
        System.setProperty("java.awt.headless", "false");
    }
    @Override
    public void run() {
        try {
            Robot robot = new Robot();
            String format = "jpg";
            String fileName = FileConfig.storeDirectory
                    + File.separator + "SC_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(CommonConstants.FORMAT_TIME)) + "." + format;
            Files.createDirectories(Paths.get(FileConfig.storeDirectory));
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            File fileImage = new File(fileName);
            ImageIO.write(screenFullImage, format, fileImage);
            fileIntegration.uploadScreenShot(fileImage);
        } catch (AWTException | IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex) {
            ex.printStackTrace();
        }
    }
}
