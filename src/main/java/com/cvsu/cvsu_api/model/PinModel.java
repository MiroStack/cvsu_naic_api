package com.cvsu.cvsu_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PinModel {
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPinPassword() {
        return pinPassword;
    }

    public void setPinPassword(String pinPassword) {
        this.pinPassword = pinPassword;
    }

    private Long userId;
    private String password;
    private String pinPassword;
}
