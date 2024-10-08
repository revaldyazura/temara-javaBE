package com.temara.backend.security.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtRegisterRequest {
  private String name;
  private String email;
  private String password;
  private List<String> roleCode;  
}
