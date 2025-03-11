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
    public ResponseModel addAccount(String firstname, String middlename, String lastname, String username, String password, Long createdById) {
        ResponseModel res = new ResponseModel();
        try{
            Optional<UserProfileEntity> creatorEntityOptional = userProfileRepository.findById(createdById);

            if(creatorEntityOptional.isPresent()){
                UserProfileEntity creatorEntity = creatorEntityOptional.get();
                System.out.println(creatorEntity.getUserId());
                Optional<AuthEntity> authEntityOptional = authRepository.findById(creatorEntity.getUserId());

             if(authEntityOptional.isPresent()){
                   AuthEntity creatorAuthEntity = authEntityOptional.get();
                   AuthEntity authEntity = new AuthEntity();
                    authEntity.setPosition(creatorAuthEntity.getPosition().equals("PPSS")?"PPSS":"HR");
                    authEntity.setUsername(username);
                    authEntity.setStatus("active");
                    authEntity.setPassword(passwordEncoder.encode(password));
                    authEntity.setCreatedById(creatorAuthEntity.getId());
                    //remove the condition where the created account can be superadmin. It is set to be admin only.
//                    if(canCreateAccount){
//                        authEntity.setRoleId(1L);
//                    }
//                    else{
//                        authEntity.setRoleId(2L);
//                    }
                    authEntity.setRoleId(2L);
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
                   }
                   else{
                     res.setSuccess(false);
                     res.setStatusCode(500);
                     res.setMessage("Failed to create an account.");
                   }
                }else{
                    res.setSuccess(false);
                    res.setStatusCode(500);
                    res.setMessage("You are not allowed to create new account.");

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
            System.out.println(oldPassword);

                Optional<UserProfileEntity> userProfileEntityOptional = userProfileRepository.findById(id);
                  if(userProfileEntityOptional.isEmpty()){
                      res.setSuccess(false);
                      res.setStatusCode(404);
                      res.setMessage("Account not found. No user profile.");
                      return res;
                  }
            UserProfileEntity userProfileEntity = userProfileEntityOptional.get();
            Optional<AuthEntity> authEntityOptional = authRepository.findById(userProfileEntity.getUserId());
            if(authEntityOptional.isEmpty()){
                res.setSuccess(false);
                res.setStatusCode(404);
                res.setMessage("Account not found.");
                return res;
            }
            AuthEntity authEntity = authEntityOptional.get();
            if(passwordEncoder.matches(oldPassword, authEntity.getPassword())){
                authEntity.setUsername(username);
                authEntity.setPassword(passwordEncoder.encode(password));
                authRepository.save(authEntity);
                res.setMessage("Account successfully updated");
                res.setStatusCode(200);
                res.setSuccess(true);
            }
            else{
                res.setMessage("Account user not found. Please check your password.");
                res.setStatusCode(404);
                res.setSuccess(false);
                return res;
            }
            return res;


        } catch (Exception e) {
                res.setStatusCode(500);
                res.setSuccess(false);
              res.setMessage("An error occurred: " + e.getMessage());
              return res;
        }
    }
}
