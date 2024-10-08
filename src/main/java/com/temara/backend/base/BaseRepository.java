package com.temara.backend.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
  
  @Override
  public <S extends T> S save(S object);

  @Override
  public <S extends T> List<S> saveAll(Iterable<S> entities);
}
