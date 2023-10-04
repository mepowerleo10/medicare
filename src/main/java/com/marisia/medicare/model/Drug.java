package com.marisia.medicare.model;

import java.util.List;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
public class Drug {
  @Id
  @GeneratedValue
  private Long id;

  @Column(unique = true)
  @NotNull
  private String name;

  @NotNull
  @Min(value = 200, message = "The price can not be smaller than 200.00")
  private Float price;

  @NotNull
  private String seller;

  @NotNull
  @Size(min = 20, max = 360, message = "Description should be between 20 and 360 characters")
  String description;
  Boolean enabled;

  @ManyToMany(targetEntity = CartItem.class)
  List<CartItem> cartItems;

  @NotNull
  @Min(value = 0, message = "The quantity can not be less than 0")
  Integer quantity = 0;

  public Drug() {
  }

  public Drug(String name, Float price, String description, Boolean enabled, String seller) {
    this.name = name;
    this.price = price;
    this.description = description;
    this.enabled = enabled;
    this.seller = seller;
  }

  public Drug(Long id, String name, Float price, String description, Boolean enabled, String seller) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.description = description;
    this.enabled = enabled;
    this.seller = seller;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public String getSeller() {
    return seller;
  }

  public void setSeller(String seller) {
    this.seller = seller;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean isEnabled() {
    return enabled;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isNew() {
    return id == null;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public boolean inStock() {
    return quantity > 0;
  }

  public List<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<CartItem> cartItems) {
    this.cartItems = cartItems;
  }

}
