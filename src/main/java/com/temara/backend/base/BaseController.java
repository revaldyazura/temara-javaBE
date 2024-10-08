package com.temara.backend.base;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.temara.backend.entity.Developer;
import com.temara.backend.entity.Role;
import com.temara.backend.entity.User;

public class BaseController {

  public Developer getCurrentUser() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext != null) {
      Authentication authentication = securityContext.getAuthentication();
      if (authentication != null && authentication.getPrincipal() instanceof User) {
        return (Developer) authentication.getPrincipal();
      } else {
        return null;
      }
    }
    return null;
  }

  public UUID getCurrentUserId() {
    return getCurrentUser() != null ? getCurrentUser().getUserId() : null;
  }

  public boolean hasAuthority(String[] authority) {
    Set<Role> currentAuthority = getCurrentUser() != null ? getCurrentUser().getRoles() : new HashSet<Role>();
    List<String> authorityAllowed = Arrays.asList(authority);
    boolean result = false;

    for (Role auth : currentAuthority) {
      result = result || authorityAllowed.contains(auth.getRoleCode());
    }
    return result;
  }

  public String toString(Object object) {
    return object == null ? "" : object.toString();
  }
}
