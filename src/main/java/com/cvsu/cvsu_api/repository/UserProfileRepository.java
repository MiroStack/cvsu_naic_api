package com.cvsu.cvsu_api.repository;

import com.cvsu.cvsu_api.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    UserProfileEntity findByUserId(Long id);
    UserProfileEntity findByEmployeeNo(String employeeNo);
}
