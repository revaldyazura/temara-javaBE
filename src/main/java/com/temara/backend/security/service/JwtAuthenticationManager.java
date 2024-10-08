package com.temara.backend.security.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.temara.backend.entity.User;

@Service
public class JwtAuthenticationManager implements AuthenticationManager {

  @Autowired
  private JwtUserDetailsService jwtUserDetailsService;

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    User user = (User) jwtUserDetailsService.loadUserByUsername(username);

    if (user == null || !user.getUsername().equalsIgnoreCase(username)) {
      throw new BadCredentialsException("Username not found");
    }

    if (!password.equals(user.getPassword())) {
      throw new BadCredentialsException("Wrong password");
    }

    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

    return new UsernamePasswordAuthenticationToken(user, password, authorities);
  }
}
