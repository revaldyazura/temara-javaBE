package com.temara.backend.base;

import java.lang.reflect.Field;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.temara.backend.entity.Developer;
import com.temara.backend.entity.Role;
import com.temara.backend.entity.User;

public class BaseService {

  public Developer getCurrentUser() {
    SecurityContext context = SecurityContextHolder.getContext();
    if (context != null) {
      Authentication authentication = context.getAuthentication();
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

  public boolean hasAuthority(String roleCode) {
    Set<Role> currentAuthority = getCurrentUser() != null ? getCurrentUser().getRoles() : new HashSet<>();
    boolean result = false;
    for (Role authority : currentAuthority) {
      result = result || authority.getAuthority().equals(roleCode);
    }
    return result;
  }

  public static boolean allNull(Object target) {
    return Arrays.stream(target.getClass()
        .getDeclaredFields())
        .peek(f -> f.setAccessible(true))
        .map(f -> getFieldValue(f, target))
        .allMatch(Objects::isNull);
  }

  public static boolean allEmpty(Object target) {
    return Arrays.stream(target.getClass().getDeclaredFields()).peek(f -> f.setAccessible(true))
        .map(f -> getFieldValue(f, target)).allMatch(BaseService::isEmpty);
  }

  public static Object getFieldValue(Field field, Object target) {
    try {
      return field.get(target);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean isEmpty(Object obj) {
    return obj == null || (obj.toString()).trim().equals("");
  }
}
