package com.temara.backend.service;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.temara.backend.base.BaseService;
import com.temara.backend.entity.Developer;
import com.temara.backend.entity.Role;
import com.temara.backend.entity.User;
import com.temara.backend.entity.UserRole;
import com.temara.backend.repository.DeveloperRepository;
import com.temara.backend.repository.UserRepository;
import com.temara.backend.repository.UserRoleRepository;
import com.temara.backend.security.JwtUtil;
import com.temara.backend.security.model.JwtRegisterRequest;
import com.temara.backend.security.model.JwtResponse;

@Service
public class UserService extends BaseService{
  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Autowired
  private DeveloperRepository developerRepository;

  @Autowired
  private UserRepository userRepository;

  public JwtResponse register(JwtRegisterRequest request){
    Pattern emailPattern = Pattern.compile("^\\w+@mail\\.com$");
    Matcher matcher = emailPattern.matcher(request.getEmail());

    if (!matcher.find()) {
        return null;
    }

    User userExist = userRepository.findByEmail(request.getEmail()).orElse(null);

    if (userExist!=null) {
      return JwtResponse.builder().jwtToken(null).build();
    }

    Set<Role> roles = new HashSet<>();
    for (String roleCode : request.getRoleCode()) {
        Role role = new Role();
        role.setRoleCode(roleCode);
        roles.add(role);
    }

//    String sha256hex = Hashing.sha256().hashString(request.getEmail()+request.getPassword(), StandardCharsets.UTF_8).toString();

    Developer developer = Developer.builder().name(request.getName()).email(request.getEmail()).password(request.getPassword()).roles(roles).build();

    developer = developerRepository.save(developer);

    for (Role role : roles) {
        UserRole userRole = new UserRole(developer.getUserId(), role.getRoleCode());
        userRoleRepository.save(userRole);
    }

    String jwtToken = jwtUtil.generateToken(developer);

    return JwtResponse.builder().jwtToken(jwtToken).build();

  }
}
