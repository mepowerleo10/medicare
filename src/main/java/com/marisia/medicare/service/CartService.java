package com.marisia.medicare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marisia.medicare.model.AppUser;
import com.marisia.medicare.model.Cart;
import com.marisia.medicare.repository.CartRepository;

@Service
public class CartService {
  @Autowired
  CartRepository repository;

  public Cart getActiveCartForUser(AppUser user) {
    return repository.findByOwnerAndIsCheckedOutFalse(user).orElse(new Cart(user));
  }

  public Iterable<Cart> getOrders() {
    return repository.isCheckedOutTrue();
  }

  public Cart save(Cart cart) {
    return repository.save(cart);
  }
}
