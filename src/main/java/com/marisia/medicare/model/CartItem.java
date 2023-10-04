package com.marisia.medicare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CartItem {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "drug_id", nullable = false, updatable = false)
  private Drug drug;

  @ManyToOne(optional = false)
  @JoinColumn(name = "cart_id", nullable = false, updatable = false)
  private Cart cart;

  private Integer quantity = 0;

  public CartItem() {
  }

  public CartItem(Drug drug, Cart cart) {
    this.drug = drug;
    this.cart = cart;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Drug getDrug() {
    return drug;
  }

  public void setDrug(Drug drug) {
    this.drug = drug;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getName() {
    return drug.getName();
  }

  public String getDescription() {
    return drug.getDescription();
  }

  public float getTotalPrice() {
    return drug.getPrice() * quantity;
  }

  public double getPerItemPrice() {
    return drug.getPrice();
  }

}
