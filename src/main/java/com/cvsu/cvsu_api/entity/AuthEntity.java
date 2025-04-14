package com.cvsu.cvsu_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_user")
public class AuthEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "createdby_id")
    private Long createdById;

    @Column(name = "status")
    private String status;

    @Column(name = "pinPassword")
    private String pinPassword;

    public String getPinPassword() {
        return pinPassword;
    }

    public void setPinPassword(String pinPassword) {
        this.pinPassword = pinPassword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Column(name = "position")
    private String position;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
