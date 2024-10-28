package com.temara.backend.security.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.temara.backend.base.BaseService;
import com.temara.backend.entity.Role;
import com.temara.backend.entity.RoleMenu;
import com.temara.backend.entity.User;
import com.temara.backend.repository.RoleMenuRepository;
import com.temara.backend.security.JwtUtil;
import com.temara.backend.security.model.JwtRequest;
import com.temara.backend.security.model.JwtResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticateService extends BaseService {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private JwtUserDetailsService userDetailsService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private RoleMenuRepository roleMenuRepository;

  public JwtResponse authenticate(JwtRequest request) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    if (authentication.isAuthenticated()) {
      final UserDetails userDetails = userDetailsService
          .loadUserByUsername(request.getUsername());

      Set<Role> roles = ((User) userDetails).getRoles();
      Set<String> roleCodes = new HashSet<>();
      for (Role role : roles)
        roleCodes.add(role.getRoleCode());

      Set<RoleMenu> roleMenus = roleMenuRepository.findByRoleCodeIn(roleCodes);
      Set<Integer> menuIds = new HashSet<>();
      for (RoleMenu roleMenu : roleMenus)
        menuIds.add(roleMenu.getMenuId());

      return JwtResponse.builder().jwtToken(jwtUtil.generateToken(userDetails)).menu(menuIds)
      .userId(getCurrentUserId()).build();

    } else {
      throw new UsernameNotFoundException("Invalid User");
    }

  }
}
