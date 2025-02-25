package com.cvsu.cvsu_api.serviceImp;

import com.cvsu.cvsu_api.entity.AuthEntity;
import com.cvsu.cvsu_api.entity.UserProfileEntity;
import com.cvsu.cvsu_api.entity.UserRoleEntity;
import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.model.UserProfileModel;
import com.cvsu.cvsu_api.repository.AuthRepository;
import com.cvsu.cvsu_api.repository.UserProfileRepository;
import com.cvsu.cvsu_api.repository.UserRoleRepository;
import com.cvsu.cvsu_api.service.AccountService;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImp implements AccountService {
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResponseModel addAccount(String firstname, String middlename, String lastname, String username, String password, Long createdById, boolean canCreateAccount) {
        ResponseModel res = new ResponseModel();
        try{
            Optional<AuthEntity> creatorEntityOptional = authRepository.findById(createdById);
            if(creatorEntityOptional.isPresent()){
                AuthEntity creatorEntity = creatorEntityOptional.get();
                if(creatorEntity.getRoleId() == 1){
                    AuthEntity authEntity = new AuthEntity();
                    authEntity.setUsername(username);
                    authEntity.setPassword(passwordEncoder.encode(password));
                    authEntity.setPosition("PPSS");
                    authEntity.setCreatedById(createdById);
                    if(canCreateAccount){
                        authEntity.setRoleId(1L);
                    }
                    else{
                        authEntity.setRoleId(2L);
                    }
                    authRepository.save(authEntity);

                    UserProfileEntity userProfileEntity = new UserProfileEntity();
                    userProfileEntity.setFirstname(firstname);
                    userProfileEntity.setMiddlename(middlename);
                    userProfileEntity.setLastname(lastname);
                    userProfileEntity.setUserId(authEntity.getId());
                    userProfileRepository.save(userProfileEntity);

                    UserRoleEntity userRoleEntity = new UserRoleEntity();
                    userRoleEntity = userRoleRepository.findByRoleId(authEntity.getRoleId());

                    UserProfileModel userProfileModel = new UserProfileModel();
                    userProfileModel.setFullName(userProfileEntity.getFirstname() + " "+ userProfileEntity.getMiddlename() + " "+ userProfileEntity.getLastname());
                    userProfileModel.setPosition(authEntity.getPosition());
                    userProfileModel.setRoleName(userRoleEntity.getRoleName());
                    userProfileModel.setId(authEntity.getId());
                    res.setMessage("Successfully add user with name " +userProfileModel.getFullName());
                    res.setStatusCode(200);
                    res.setSuccess(true);

                }else{
                    res.setSuccess(false);
                    res.setStatusCode(500);
                    res.setMessage("You are not allowed to create new account.");

                }

            }
            return res;

        } catch (RuntimeException e) {
            res.setSuccess(false);
            res.setStatusCode(500);
            res.setMessage("Failed to create new user.");
            return res;

        }
    }

    @Override
    public ResponseModel editAccount(String username, String oldPassword, String password, Long id) {
        ResponseModel res = new ResponseModel();
        try {
            Optional<UserProfileEntity> userOptional = userProfileRepository.findById(id);

            if (userOptional.isPresent()) {
                UserProfileEntity userProfileEntity = userOptional.get();
                Optional<AuthEntity> authEntityOptional = authRepository.findById(userProfileEntity.getUserId());
                   if(authEntityOptional.isPresent()){
                       AuthEntity authEntity = authEntityOptional.get();
                       authEntity.setUsername(username);
                       authEntity.setPassword(passwordEncoder.encode(password));
                       authRepository.save(authEntity);
                       res.setMessage("Account successfully updated");
                       res.setStatusCode(200);
                       res.setSuccess(true);
                       return res;
                   }
                   else{
                       res.setMessage("Account user id not found.");
                       res.setStatusCode(200);
                       res.setSuccess(true);
                       return res;
                   }


            } else {
                res.setMessage("User not found");
                res.setStatusCode(500);
                res.setSuccess(false);
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                return res;
            }
        } catch (Exception e) {
                res.setStatusCode(500);
                res.setSuccess(false);
              res.setMessage("\"An error occurred: \" + e.getMessage()");
              return res;
        }
    }
}
