package com.cvsu.cvsu_api.serviceImp;

import com.cvsu.cvsu_api.entity.AuthEntity;
import com.cvsu.cvsu_api.entity.UserProfileEntity;
import com.cvsu.cvsu_api.entity.UserRoleEntity;
import com.cvsu.cvsu_api.model.AuthResponseModel;
import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.model.UserProfileModel;
import com.cvsu.cvsu_api.repository.AuthRepository;
import com.cvsu.cvsu_api.repository.UserProfileRepository;
import com.cvsu.cvsu_api.repository.UserRoleRepository;
import com.cvsu.cvsu_api.service.AuthService;
import com.cvsu.cvsu_api.utl.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private JwtUtil jwtUtil;



    @Override
    public AuthResponseModel  login(String username, String password) {
        AuthResponseModel authResponseModel = new AuthResponseModel();
        UserProfileModel userProfileModel = new UserProfileModel();
        AuthEntity authEntity = authRepository.findByUsername(username);


        // Check if user exists
        if (authEntity == null) {
            authResponseModel.setMessage("Account not found");
            authResponseModel.setToken(null);
            authResponseModel.setSuccess(false);
            authResponseModel.setStatusCode(404);
            return authResponseModel; // or throw an exception
        }
        if(authEntity.getStatus().equals("inactive")){
            authResponseModel.setMessage("Your account is inactive.");
            authResponseModel.setToken(null);
            authResponseModel.setSuccess(false);
            authResponseModel.setStatusCode(401);
            return authResponseModel; // or throw an exception
        }


        System.out.println("Stored Hash: " + authEntity.getPassword()); // Debugging

        // Verify password
        if (passwordEncoder.matches(password, authEntity.getPassword())) {
            UserProfileEntity userProfileEntity = userProfileRepository.findByUserId(authEntity.getId());;
            UserRoleEntity  userRoleEntity = userRoleRepository.findByRoleId(authEntity.getRoleId());
            userProfileModel.setFullName(userProfileEntity.getFirstname() + " " + userProfileEntity.getMiddlename() + " " + userProfileEntity.getLastname());
            userProfileModel.setRoleName(userRoleEntity.getRoleName());
            userProfileModel.setId(userProfileEntity.getId());
            userProfileModel.setPassword(authEntity.getPassword());
            userProfileModel.setPosition(authEntity.getPosition());
            userProfileModel.setUsername(authEntity.getUsername());
            userProfileModel.setStatus(authEntity.getStatus());
            String token = jwtUtil.generateToken(userProfileModel);
            authResponseModel.setStatusCode(200);
            authResponseModel.setSuccess(true);
            authResponseModel.setMessage("Successfully login.");
            authResponseModel.setToken(token);
            return authResponseModel;
        }
        else{
            authResponseModel.setStatusCode(404);
            authResponseModel.setSuccess(true);
            authResponseModel.setMessage("Incorrect username or password. Please try again.");
            authResponseModel.setToken(null);
            return authResponseModel;
        }


    }

    @Override
    public List<UserProfileModel> fetchUser(String position) {
      try{
          List<UserProfileModel> list = new ArrayList<>();
          List<AuthEntity> authEntityList = authRepository.findByPosition(position);
          for(AuthEntity entity : authEntityList){
              UserProfileEntity u = userProfileRepository.findByUserId(entity.getId());
              UserProfileModel m = new UserProfileModel();
              UserRoleEntity ure = userRoleRepository.findByRoleId(entity.getRoleId());
              m.setFullName(u.getFirstname() +" "+u.getMiddlename()+" "+u.getLastname());
              m.setStatus(entity.getStatus());
              m.setId(u.getUserId());
              m.setPosition(entity.getPosition());
              m.setRoleName(ure.getRoleName());
              m.setUsername(entity.getUsername());
              list.add(m);
          }
          return list;
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
    }

    @Override
    public ResponseModel updateStatus(Long id) {
        ResponseModel r = new ResponseModel();
       try{
          Optional<AuthEntity> authEntityOptional = authRepository.findById(id);
          if(authEntityOptional.isPresent()){
             AuthEntity authEntity = authEntityOptional.get();
             if(authEntity.getRoleId() == 2) {
                 if (authEntity.getStatus().equals("active")) {
                     authEntity.setStatus("inactive");
                 } else {
                     authEntity.setStatus("active");
                 }

                     r.setMessage("Account status updated successfully.");
                     r.setStatusCode(200);
                     r.setSuccess(true);
                 authRepository.save(authEntity);

             }
             else{
                 r.setMessage("Failed to update status. You are not permitted to update SuperAdmin account.");
                 r.setStatusCode(401);
                 r.setSuccess(false);
             }


              return r;
          }
          else{
              r.setMessage("Failed to update status. Account not found.");
              r.setStatusCode(404);
              r.setSuccess(false);
          }
          return r;

       } catch (Exception e) {
           r.setStatusCode(500);
           r.setSuccess(false);
           r.setMessage("An error occured: "+e.getMessage());
           return r;
       }


    }


}
