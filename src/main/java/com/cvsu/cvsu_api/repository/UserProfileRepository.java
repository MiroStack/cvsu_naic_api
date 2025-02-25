package com.cvsu.cvsu_api.repository;

import com.cvsu.cvsu_api.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    UserProfileEntity findByUserId(Long id);

}
