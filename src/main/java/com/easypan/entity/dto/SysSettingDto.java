package com.easypan.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingDto implements Serializable {
    private String registerMailTitle = "邮箱验证码";
    private String registerEmailContent = "您好，您的邮箱验证码是：%s，15分钟有效哦";
    private long userInitUseSpace=5*1024*1024l;

    public String getRegisterMailTitle() {
        return registerMailTitle;
    }

    public void setRegisterMailTitle(String registerMailTitle) {
        this.registerMailTitle = registerMailTitle;
    }

    public String getRegisterEmailContent() {
        return registerEmailContent;
    }

    public void setRegisterEmailContent(String registerEmailContent) {
        this.registerEmailContent = registerEmailContent;
    }

    public long getUserInitUseSpace() {
        return userInitUseSpace;
    }

    public void setUserInitUseSpace(long userInitUseSpace) {
        this.userInitUseSpace = userInitUseSpace;
    }
}
