package com.cvsu.cvsu_api.service;

import com.cvsu.cvsu_api.model.ResponseModel;
import com.cvsu.cvsu_api.model.UserProfileModel;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseModel addAccount(String firstname, String middlename, String lastname, String username, String password, Long createdById, boolean canCreateAccount);
    ResponseModel editAccount(String username, String oldPassword, String password, Long id);
}
