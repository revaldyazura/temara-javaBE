package com.temara.backend.security.model;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import lombok.Builder;

@Builder
public class JwtResponse implements Serializable {

  private static final long serialVersionUID = -8091879091924046844L;
  private final String jwtToken;
  private Set<Integer> menu;
  private UUID userId;

  public JwtResponse(String jwtToken, Set<Integer> menuId, UUID userId) {
    this.jwtToken = jwtToken;
    this.menu = menuId;
    this.userId = userId;
  }


  public String getJwtToken() {
    return this.jwtToken;
  }

  public Set<Integer> getMenu() {
    return this.menu;
  }

  public UUID getUserId() {
    return this.userId;
  }
  
}
