package com.cvsu.cvsu_api.repository;

import com.cvsu.cvsu_api.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findByRoleId(Long roleId);

}
