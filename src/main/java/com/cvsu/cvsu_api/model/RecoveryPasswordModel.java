package com.cvsu.cvsu_api.model;

public class RecoveryPasswordModel {


    public String getPinPassword() {
        return pinPassword;
    }

    public void setPinPassword(String pinPassword) {
        this.pinPassword = pinPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }
    private String employeeNo;
    private String pinPassword;
    private String newPassword;
}
