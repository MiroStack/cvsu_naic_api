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
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
                    LocalDate today = LocalDate.now();
                    int year = today.getYear();
                    int month = today.getMonthValue();
                    int day = today.getDayOfMonth();
                    String monthTxt = month < 10? "0"+month : String.valueOf(month);
                    String dayTxt = day < 10 ? "0"+day : String.valueOf(day);
                    String userIdTxt = authEntity.getId() < 10? "0"+authEntity.getId():String.valueOf(authEntity.getId());
                    userProfileEntity.setEmployeeNo(year+monthTxt+dayTxt+userIdTxt);
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

    @Override
    public ResponseModel addRecoveryPassword(Long id, String password, String pinPassword) {

        ResponseModel res = new ResponseModel();
        try{
           Optional<UserProfileEntity> userProfileEntityOptional = userProfileRepository.findById(id);
           if(userProfileEntityOptional.isPresent()){
               UserProfileEntity userProfileEntity = userProfileEntityOptional.get();
               Optional<AuthEntity> authEntityOptional = authRepository.findById(userProfileEntity.getUserId());
               if(authEntityOptional.isPresent()){

                   AuthEntity authEntity = authEntityOptional.get();
                   if(!encoder.matches(password, authEntity.getPassword())){
                       res.setMessage("Incorrect password! Please try again.");
                       res.setSuccess(false);
                       res.setStatusCode(404);
                       return res;
                   }
                   if(authEntity.getPinPassword() != null && !authEntity.getPinPassword().trim().isEmpty()){
                       res.setMessage("Failed to add recovery password. You already set your pin password for your account.");
                       res.setSuccess(false);
                       res.setStatusCode(409);
                       return res;
                   }
                   authEntity.setPinPassword(pinPassword);
                   authRepository.save(authEntity);
                   res.setMessage("Recovery password successfully saved");
                   res.setSuccess(true);
                   res.setStatusCode(200);


               }else{
                   res.setMessage("User id not found.");
                   res.setStatusCode(404);
                   res.setSuccess(false);

               }
           }
           else{
               res.setMessage("Id not found.");
               res.setStatusCode(404);
               res.setSuccess(false);
           }

           return res;
        }catch(Exception e){
            res.setMessage("An error occurred: "+e);
            res.setStatusCode(500);
            res.setSuccess(false);
            return res;

        }
    }

    @Override
    public ResponseModel editRecoveryPassword(Long id,  String password, String newPinPassword) {
        ResponseModel res = new ResponseModel();
        try{
            Optional<UserProfileEntity> userProfileEntityOptional = userProfileRepository.findById(id);
            if(userProfileEntityOptional.isPresent()){
                UserProfileEntity userProfileEntity = userProfileEntityOptional.get();
                Optional<AuthEntity> authEntityOptional = authRepository.findById(userProfileEntity.getUserId());
                if(authEntityOptional.isPresent()){
                    AuthEntity authEntity = authEntityOptional.get();
                    if(!encoder.matches(password, authEntity.getPassword())){
                        res.setMessage("Incorrect password! Please try again.");
                        res.setStatusCode(404);
                        res.setSuccess(false);
                        return res;
                    }
                    authEntity.setPinPassword(newPinPassword);
                    authRepository.save(authEntity);
                    res.setMessage("Recovery password successfully updated");
                    res.setSuccess(true);
                    res.setStatusCode(200);

                }
                else{
                    res.setMessage("User id not found.");
                    res.setSuccess(false);
                    res.setStatusCode(404);
                }
            }
            else{
                res.setMessage("Id not found.");
                res.setSuccess(false);
                res.setStatusCode(404);
            }

            return res;
        } catch (Exception e) {
            res.setMessage("An error occurred: "+e);
            res.setStatusCode(500);
            res.setSuccess(false);
            return res;
        }
    }

    @Override
    public ResponseModel recoverPassword(String employeeNo, String pinPassword, String newPassword) {
        ResponseModel res = new ResponseModel();
        try{
           UserProfileEntity userProfileEntity = userProfileRepository.findByEmployeeNo(employeeNo);
           if(userProfileEntity == null){
               res.setMessage("User id not found.");
               res.setSuccess(false);
               res.setStatusCode(404);
               return res;
           }
           Long userId = userProfileEntity.getUserId();
           Optional<AuthEntity> authEntityOptional = authRepository.findById(userId);
           if(authEntityOptional.isPresent()){
               AuthEntity authEntity = authEntityOptional.get();
               if(!pinPassword.equals(authEntity.getPinPassword())){
                   res.setMessage("Incorrect pin password! Please try again.");
                   res.setStatusCode(404);
                   res.setSuccess(false);
                   return res;
               }
               authEntity.setPassword(passwordEncoder.encode(newPassword));
               authRepository.save(authEntity);
               res.setMessage("Password successfully updated.");
               res.setSuccess(true);
               res.setStatusCode(200);
           }
            return res;

        } catch (Exception e) {
            res.setMessage("An error occurred: "+e);
            res.setStatusCode(500);
            res.setSuccess(false);
            return res;
        }

    }

    @Override
    public ResponseModel searchEmployee(String employeeNo) {
        ResponseModel res = new ResponseModel();
        try{
            UserProfileEntity entity = userProfileRepository.findByEmployeeNo(employeeNo);

            if(entity == null){
                res.setMessage("Can't find the employee with the given employee number.");
                res.setStatusCode(404);
                res.setSuccess(false);
                return res;
            }
            res.setMessage("Successfully find the employee with the given employee number.");
            res.setStatusCode(200);
            res.setSuccess(true);
            return res;
        } catch (Exception e) {
            res.setMessage("An error occurred: "+e);
            res.setStatusCode(500);
            res.setSuccess(false);
            return res;
        }
    }
}
