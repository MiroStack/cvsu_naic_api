package com.cvsu.cvsu_api.service;

import com.cvsu.cvsu_api.entity.UserProfileEntity;
import com.cvsu.cvsu_api.model.UserProfileModel;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    UserProfileModel login(String username, String password);

}
