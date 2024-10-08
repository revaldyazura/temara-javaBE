package com.temara.backend.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "developers")
public class Developer extends User {

  private static final long serialVersionUID = 4016075334259561214L;

  private String developerId;

  private String position;

  private String domicile;

  public Developer(UUID userId) {
    super.setUserId(userId);
  }
}
