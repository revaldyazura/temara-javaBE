package com.temara.backend.repository;

import org.springframework.stereotype.Repository;

import com.temara.backend.base.BaseRepository;
import com.temara.backend.entity.Role;

@Repository
public interface RoleRepository extends BaseRepository<Role, String>{
    
}
