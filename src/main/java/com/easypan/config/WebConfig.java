package com.easypan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebConfig {
    @Value("${spring.mail.username}")
    private String userName;

    @Value("${admin.emails}")
    private String admin;

    @Value("${project.folder}")
    private String folder;

    public String getFolder() {
        return folder;
    }

    public String getUserName() {
        return userName;
    }

    public String getAdmin(){
        return admin;
    }
}
