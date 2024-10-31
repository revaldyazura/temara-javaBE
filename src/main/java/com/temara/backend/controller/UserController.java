package com.temara.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.temara.backend.base.BaseController;
import com.temara.backend.security.model.JwtRegisterRequest;
import com.temara.backend.security.model.JwtResponse;
import com.temara.backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping("/users")
@RestController
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<Object> registerUser(@RequestBody JwtRegisterRequest request) {
        JwtResponse response = userService.register(request);

        if (response == null) 
            return ResponseEntity.badRequest().body(String.format("Email %s invaldi", request.getEmail()));
        
        else if(response.getJwtToken() == null)
            return ResponseEntity.badRequest().body(String.format("Email %s Used, please login / change the email", request.getEmail()));
        
        else 
            return ResponseEntity.ok(response);
        
    }
    
    
}