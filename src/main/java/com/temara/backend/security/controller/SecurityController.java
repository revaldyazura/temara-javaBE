package com.temara.backend.security.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.hash.Hashing;
import com.temara.backend.security.model.JwtRequest;
import com.temara.backend.security.model.JwtResponse;
import com.temara.backend.security.service.AuthenticateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/security")
@RestController
public class SecurityController {

  @Autowired
  private AuthenticateService authenticateService;

  @PostMapping("/authenticate")
  public JwtResponse authToken(@RequestBody JwtRequest authRequest) {
    return authenticateService.authenticate(authRequest);
  }

  @PostMapping("/hashing")
  public String hashPassword(@RequestBody JwtRequest request) {
    // TODO: process POST request
    String hashCredential = Hashing.sha256()
        .hashString(request.getUsername() + request.getPassword(), StandardCharsets.UTF_8).toString();
    return hashCredential;
  }

}
