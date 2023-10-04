package com.marisia.medicare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.marisia.medicare.model.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Integer> {
  @Query("FROM AppUser WHERE username = ?1 AND roles NOT LIKE '%SYSTEM%'")
  Optional<AppUser> findByUsername(String username);

  @Query("FROM AppUser WHERE roles LIKE 'USER'")
  Iterable<AppUser> findAllCustomers();
}
