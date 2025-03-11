package com.cvsu.cvsu_api.service;

import com.cvsu.cvsu_api.entity.AuthEntity;
import com.cvsu.cvsu_api.entity.UserProfileEntity;
import com.cvsu.cvsu_api.model.AuthResponseModel;
import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.model.UserProfileModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthService {
    AuthResponseModel  login(String username, String password);
    List<UserProfileModel> fetchUser(String position);
    ResponseModel updateStatus(Long id);
}
