package com.temara.backend.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.temara.backend.entity.RoleMenu.MenuRoleId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(MenuRoleId.class)
@Table(name = "role_menus")
public class RoleMenu implements Serializable {

  private static final long serialVersionUID = -3669424141436843719L;

  @Id
  @Column(name = "role_code")
  private String roleCode;

  @Id
  @Column(name = "menu_id")
  private Integer menuId;

  @Column(name = "created_date")
  private Timestamp createdDate;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "last_updated_date")
  private Timestamp lastUpdatedDate;

  @Column(name = "last_updated_by")
  private String lastUpdatedBy;

  @Data
  public static class MenuRoleId implements Serializable {

    private String roleCode;
    private Integer menuId;

  }
}
