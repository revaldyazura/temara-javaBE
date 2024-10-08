package com.temara.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.temara.backend.base.BaseRepository;
import com.temara.backend.entity.UserRole;
import com.temara.backend.entity.UserRole.UserRoleId;


@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, UserRoleId> {
  
  public List<UserRole> findByUserId(UUID userId);

  public List<UserRole> findByRoleCode(String roleCode);
}
