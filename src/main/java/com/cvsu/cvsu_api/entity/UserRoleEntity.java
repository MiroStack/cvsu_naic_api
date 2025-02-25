package com.cvsu.cvsu_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ref_role")
public class UserRoleEntity {
  @Id
  @Column(name = "role_id")
    Long roleId;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Column(name = "role_name")
   String roleName;
}
