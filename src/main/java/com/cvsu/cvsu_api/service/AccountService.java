package com.cvsu.cvsu_api.service;

import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.model.UserProfileModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    ResponseModel addAccount(String firstname, String middlename, String lastname, String username, String password, Long createdById);
    ResponseModel editAccount(String username, String oldPassword, String password, Long id);

}
