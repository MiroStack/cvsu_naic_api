package com.cvsu.cvsu_api.repository;


import com.cvsu.cvsu_api.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    AuthEntity findByUsername(String username);
    AuthEntity findByPassword(String oldPassword);
    List<AuthEntity> findByPosition(String position);

}
