package com.temara.backend.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.temara.backend.entity.Developer;
import com.temara.backend.entity.Role;
import com.temara.backend.entity.UserRole;
import com.temara.backend.repository.DeveloperRepository;
import com.temara.backend.repository.UserRoleRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Autowired
  private DeveloperRepository developerRepository;

  @Autowired
  private UserRoleRepository userRoleRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Developer developer = developerRepository.findByEmail(username).orElse(null);
    if (developer == null) {
      throw new UsernameNotFoundException(username);
    } else {
      List<UserRole> developerRoles = userRoleRepository.findByUserId(developer.getUserId());
      Set<Role> developerAuthorities = new HashSet<>();
      for (UserRole userRole : developerRoles) {
        developerAuthorities.add(new Role(userRole.getRoleCode()));
      }
      developer.setRoles(developerAuthorities);

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(developer, null,
          developer.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    return developer;
  }
}
