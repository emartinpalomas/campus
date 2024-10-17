package com.example.campus.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.mail")
public class MailProperties {

    private String resetPasswordUrl;
    private String resetPasswordSubject;
    private String resetPasswordText;

    public String getResetPasswordUrl() {
        return resetPasswordUrl;
    }

    public void setResetPasswordUrl(String resetPasswordUrl) {
        this.resetPasswordUrl = resetPasswordUrl;
    }

    public String getResetPasswordSubject() {
        return resetPasswordSubject;
    }

    public void setResetPasswordSubject(String resetPasswordSubject) {
        this.resetPasswordSubject = resetPasswordSubject;
    }

    public String getResetPasswordText() {
        return resetPasswordText;
    }

    public void setResetPasswordText(String resetPasswordText) {
        this.resetPasswordText = resetPasswordText;
    }
}
