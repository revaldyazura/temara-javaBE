package com.temara.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.temara.backend.base.BaseRepository;
import com.temara.backend.entity.User;


@Repository
public interface UserRepository extends BaseRepository<User, UUID>{
    public Optional<User> findByName(String name);
    public Optional<User> findByEmail(String email);
}
