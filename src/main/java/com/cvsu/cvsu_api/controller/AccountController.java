package com.cvsu.cvsu_api.controller;

import com.cvsu.cvsu_api.model.PinModel;
import com.cvsu.cvsu_api.model.RecoveryPasswordModel;
import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.model.UserProfileModel;
import com.cvsu.cvsu_api.serviceImp.AccountServiceImp;
import com.cvsu.cvsu_api.serviceImp.AuthServiceImp;
import com.cvsu.cvsu_api.utl.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"https://cvsu-portal-weld.vercel.app", "http://localhost:5501", "https://cvsu-map-godot.vercel.app"})
@RestController
@RequestMapping("cvsu")
public class AccountController {

    private final JwtUtil jwtUtil;
    private final AccountServiceImp accountServiceImp;

    @Autowired
    public AccountController(JwtUtil jwtUtil, AccountServiceImp accountServiceImp) {
        this.jwtUtil = jwtUtil;
        this.accountServiceImp = accountServiceImp;
    }

    @PostMapping("/addAccount")
    public ResponseEntity<?> addAccount(
            @RequestParam String firstname,
            @RequestParam  String middlename,
            @RequestParam  String lastname,
            @RequestParam  String username,
            @RequestParam  String password,
            @RequestParam   Long createdById
           ) {
       return new ResponseEntity<ResponseModel>(accountServiceImp.addAccount(firstname, middlename, lastname, username, password, createdById), HttpStatus.OK);
    }
    @PostMapping("/editAccount")
    public ResponseEntity<ResponseModel> editAccount(@RequestParam String username, @RequestParam String currentPassword, @RequestParam String password, @RequestParam Long id){
        return new ResponseEntity<ResponseModel>(accountServiceImp.editAccount(username, currentPassword, password, id), HttpStatus.OK);
    }

    @PostMapping("/addRecoveryPassword")
    public ResponseEntity<ResponseModel> addRecoveryPass(@RequestBody PinModel pinModel){
        try{
            return new ResponseEntity<>(accountServiceImp.addRecoveryPassword(pinModel.getUserId(), pinModel.getPassword(), pinModel.getPinPassword()), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/editRecoveryPassword")
    public ResponseEntity editRecoveryPass(@RequestBody PinModel pinModel){
        try{
            return new ResponseEntity<>(accountServiceImp.editRecoveryPassword(pinModel.getUserId(), pinModel.getPassword(), pinModel.getPinPassword()), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/recoverPassword")
    public ResponseEntity<ResponseModel> recoverPassword(@RequestBody RecoveryPasswordModel recoveryPasswordModel){
        try{
            return new ResponseEntity<>(accountServiceImp.recoverPassword(recoveryPasswordModel.getEmployeeNo(), recoveryPasswordModel.getPinPassword(), recoveryPasswordModel.getNewPassword()), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findMyAccount")
    public ResponseEntity<ResponseModel> findMyAccount(@RequestParam String employeeNo){
        try{
            return new ResponseEntity<>(accountServiceImp.searchEmployee(employeeNo), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
