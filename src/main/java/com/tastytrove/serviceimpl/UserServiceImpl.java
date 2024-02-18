package com.tastytrove.serviceimpl;

import com.tastytrove.repository.UserRepository;
import com.tastytrove.service.UserService;
import com.tastytrove.util.Logger;
import com.tastytrove.util.ResponseUtil;
import com.tastytrove.util.ResponseMassage;
import com.tastytrove.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        Logger.logg("Inside signUp with: ", requestMap);
        try {
            if(validateSignUp(requestMap)){
                User user = userRepository.findByEmail(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userRepository.save(getUserFromMap(requestMap));
                    return ResponseUtil.createResponseEntity(ResponseMassage.SUCCESSFULLY_CREATED, HttpStatus.OK);
                } else {
                    return ResponseUtil.createResponseEntity(ResponseMassage.ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                return ResponseUtil.createResponseEntity(ResponseMassage.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Logger.logg("ServiceImpl");
        }
        return ResponseUtil.createResponseEntity(ResponseMassage.ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        Logger.logg("Inside signUp with: ", requestMap);
        try {

        } catch (Exception e){
            Logger.logg("UserServiceImpl: Exception in login()");
        }
        return  ResponseUtil.createResponseEntity(ResponseMassage.ERROR_OCCURRED, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setFirstName(requestMap.get("firstName"));
        user.setLastName(requestMap.get("lastName"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        return user;
    }

    private boolean validateSignUp(Map<String, String> requestMap){
        return requestMap.containsKey("firstName") && requestMap.containsKey("lastName") &&
                requestMap.containsKey("email") && requestMap.containsKey("password");
    }
}
