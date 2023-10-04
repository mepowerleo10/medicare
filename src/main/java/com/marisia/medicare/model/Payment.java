package com.marisia.medicare.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Payment {
  @Id
  @GeneratedValue
  Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "from_id", nullable = false, updatable = false)
  AppUser from;

  @ManyToOne(optional = false)
  @JoinColumn(name = "to_id", nullable = false, updatable = false)
  AppUser to;

  @Min(200)
  Float amount;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  Date at;

  @OneToOne(mappedBy = "payment")
  Cart cart;

  @Size(min = 10)
  String phoneNumber;

  @NotBlank
  String address;

  @NotBlank
  String city;

  @NotBlank
  String account;

  @NotBlank
  String name;

  Boolean complete = false;

  public Payment() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AppUser getFrom() {
    return from;
  }

  public void setFrom(AppUser from) {
    this.from = from;
  }

  public AppUser getTo() {
    return to;
  }

  public void setTo(AppUser to) {
    this.to = to;
  }

  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public Date getAt() {
    return at;
  }

  public void setAt(Date at) {
    this.at = at;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getComplete() {
    return complete;
  }

  public void setComplete(Boolean complete) {
    this.complete = complete;
  }

}
