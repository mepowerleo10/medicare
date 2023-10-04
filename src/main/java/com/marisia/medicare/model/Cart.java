package com.marisia.medicare.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Cart {
  @Id
  @GeneratedValue
  Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "owner_id", nullable = false, updatable = false)
  AppUser owner;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  Date createdAt;

  Boolean isCheckedOut = false;

  @Temporal(TemporalType.TIMESTAMP)
  Date checkedOutAt;

  @OneToOne
  @JoinColumn(name = "payment_id", nullable = true, unique = true)
  Payment payment;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
  Map<Drug, CartItem> items = new HashMap<>();

  public Cart() {
  }

  public Cart(AppUser owner, Boolean isCheckedOut, Date checkedOutAt) {
    this.owner = owner;
    this.isCheckedOut = isCheckedOut;
    this.checkedOutAt = checkedOutAt;
  }

  public Cart(AppUser owner) {
    this.owner = owner;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AppUser getOwner() {
    return owner;
  }

  public void setOwner(AppUser owner) {
    this.owner = owner;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Boolean getIsCheckedOut() {
    return isCheckedOut;
  }

  public void setIsCheckedOut(Boolean isCheckedOut) {
    this.isCheckedOut = isCheckedOut;
  }

  public Date getCheckedOutAt() {
    return checkedOutAt;
  }

  public void setCheckedOutAt(Date checkedOutAt) {
    this.checkedOutAt = checkedOutAt;
  }

  public int size() {
    return items.size();
  }

  public void addToCart(Drug drug) {
    var item = items.getOrDefault(drug, new CartItem(drug, this));
    item.setQuantity(item.getQuantity() + 1);
    items.put(drug, item);
  }

  public Double getTotalPrice() {
    return items
        .values()
        .stream()
        .mapToDouble(CartItem::getTotalPrice)
        .sum();
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public Map<Drug, CartItem> getItems() {
    return items;
  }

  public void setItems(Map<Drug, CartItem> items) {
    this.items = items;
  }

  public String by() {
    return owner.getUsername();
  }

  public Float getValue() {
    return payment.getAmount();
  }

  public String getItemsList() {
    return items
        .values()
        .stream()
        .map(
            i -> String.format("%s (%d)", i.getName(), i.getQuantity()))
        .collect(Collectors.joining(","));
  }

}
