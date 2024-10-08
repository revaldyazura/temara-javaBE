package com.temara.backend.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import com.temara.backend.entity.UserRole.UserRoleId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserRoleId.class)
@Table(name = "user_roles")
public class UserRole implements Serializable {

  private static final long serialVersionUID = -7476042405147619639L;

  @Id
  @Column(name = "user_id")
  private UUID userId;

  @Id
  @Column(name = "role_code")
  private String roleCode;

  @Column(name = "created_by")
  private UUID createdBy;

  @Column(name = "created_date")
  private Timestamp createdDate;

  @Column(name = "last_updated_by")
  private UUID lastUpdatedBy;

  @Column(name = "last_updated_date")
  private Timestamp lastUpdatedDate;

  public UserRole(UUID userId, String roleCode) {
    this.userId = userId;
    this.roleCode = roleCode;
  }

  @Data
  public static class UserRoleId implements Serializable {

    private UUID userId;

    private String roleCode;

  }
}
