package com.temara.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.temara.backend.base.BaseRepository;
import com.temara.backend.entity.Developer;


@Repository
public interface DeveloperRepository extends BaseRepository<Developer, UUID> {
  
  public Optional<Developer> findByEmail(String email);
}
