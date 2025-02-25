package com.cvsu.cvsu_api.repository;


import com.cvsu.cvsu_api.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    AuthEntity findByUsername(String username);

}
