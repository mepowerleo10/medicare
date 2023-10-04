package com.marisia.medicare.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.marisia.medicare.model.AppUser;
import com.marisia.medicare.model.Cart;

public interface CartRepository extends CrudRepository<Cart, Long> {
  Optional<Cart> findByOwnerAndIsCheckedOutFalse(AppUser owner);
  Iterable<Cart> isCheckedOutTrue();
}
