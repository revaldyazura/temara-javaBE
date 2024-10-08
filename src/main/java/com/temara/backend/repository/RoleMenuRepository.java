package com.temara.backend.repository;

import java.util.Set;

import org.springframework.stereotype.Repository;

import com.temara.backend.base.BaseRepository;
import com.temara.backend.entity.RoleMenu;
import com.temara.backend.entity.RoleMenu.MenuRoleId;

@Repository
public interface RoleMenuRepository extends BaseRepository<RoleMenu, MenuRoleId> {
  
  public Set<RoleMenu> findByRoleCode(Set<String> role);
}
