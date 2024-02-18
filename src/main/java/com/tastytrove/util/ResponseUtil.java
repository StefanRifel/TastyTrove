package com.tastytrove.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<String> createResponseEntity(ResponseMassage message, HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\""+message+"\"}", httpStatus);
    }
}
