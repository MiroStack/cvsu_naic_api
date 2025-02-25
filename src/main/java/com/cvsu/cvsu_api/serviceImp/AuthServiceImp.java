package com.cvsu.cvsu_api.serviceImp;

import com.cvsu.cvsu_api.entity.AuthEntity;
import com.cvsu.cvsu_api.entity.UserProfileEntity;
import com.cvsu.cvsu_api.entity.UserRoleEntity;
import com.cvsu.cvsu_api.model.UserProfileModel;
import com.cvsu.cvsu_api.repository.AuthRepository;
import com.cvsu.cvsu_api.repository.UserProfileRepository;
import com.cvsu.cvsu_api.repository.UserRoleRepository;
import com.cvsu.cvsu_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImp implements AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserProfileModel login(String username, String password) {
        UserProfileModel userProfileModel = new UserProfileModel();
        AuthEntity authEntity = authRepository.findByUsername(username);


        // Check if user exists
        if (authEntity == null) {
            return null; // or throw an exception
        }

        System.out.println("Stored Hash: " + authEntity.getPassword()); // Debugging

        // Verify password
        if (passwordEncoder.matches(password, authEntity.getPassword())) {
            UserProfileEntity userProfileEntity = userProfileRepository.findByUserId(authEntity.getId());;
            UserRoleEntity  userRoleEntity = userRoleRepository.findByRoleId(authEntity.getRoleId());
            userProfileModel.setFullName(userProfileEntity.getFirstname() + " " + userProfileEntity.getMiddlename() + " " + userProfileEntity.getLastname());
            userProfileModel.setRoleName(userRoleEntity.getRoleName());
            userProfileModel.setId(userProfileEntity.getId());
            userProfileModel.setPosition(authEntity.getPosition());
            userProfileModel.setUsername(authEntity.getUsername());
            userProfileModel.setPassword(passwordEncoder.encode(authEntity.getPassword()));

            return userProfileModel;
        }

        return null; // Incorrect password
    }



}
