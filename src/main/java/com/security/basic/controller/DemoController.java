package com.security.basic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class DemoController {

    @GetMapping("/me")
    public ResponseEntity<String> me(){
        return new ResponseEntity<>("ME", HttpStatus.OK);
    }
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("TEST", HttpStatus.OK);
    }
}
