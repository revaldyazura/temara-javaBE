package com.temara.backend.entity;

import java.sql.Timestamp;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role implements GrantedAuthority {

  private static final long serialVersionUID = 3286458559345856332L;

  public static final String roleUser = "USER";
  public static final String roleAdmin = "ADMIN";
  public static final String roleDeveloper = "DEV";

  @Id
  private String roleCode;

  @Column(name = "created_date")
  private Timestamp createdDate;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "last_updated_date")
  private Timestamp lastUpdatedDate;

  @Column(name = "last_updated_by")
  private String lastUpdatedBy;

  @Override
  public String getAuthority() {
    return this.roleCode;
  }

  public Role(String roleCode) {
    this.roleCode = roleCode;
  }

}
