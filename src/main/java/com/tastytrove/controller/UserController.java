package com.tastytrove.controller;

import com.tastytrove.rest.UserRest;
import com.tastytrove.service.UserService;
import com.tastytrove.util.Logger;
import com.tastytrove.util.ResponseUtil;
import com.tastytrove.util.ResponseMassage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController implements UserRest {

    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        } catch (Exception e) {
            Logger.logg("UserController: Exception in signUp()");
        }
        return ResponseUtil.createResponseEntity(ResponseMassage.ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception e) {
            Logger.logg("UserController: Exception in login()");
        }
        return ResponseUtil.createResponseEntity(ResponseMassage.ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
