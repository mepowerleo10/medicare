package com.marisia.medicare.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "app_users")
public class AppUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @NotNull
  @Size(min = 4, max = 25, message = "Username must be between 4 and 25 characters long")
  @Column(unique = true)
  String username;

  String password;

  @Transient
  String confirmPassword;

  String roles;

  String avatarPath;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
  List<Cart> carts;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "to", fetch = FetchType.LAZY)
  List<Payment> paymentsReceived;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "from", fetch = FetchType.LAZY)
  List<Payment> paymentsMade;

  String account;

  public AppUser() {
  }

  public AppUser(Integer id, String username, String password, String account, String roles) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.roles = roles;
    this.account = account;
  }

  public String getRoles() {
    return roles;
  }

  public void setRoles(String roles) {
    this.roles = roles;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public boolean passwordsMatch() {
    return password.equals(confirmPassword);
  }

  public String getAvatarPath() {
    return avatarPath;
  }

  public void setAvatarPath(String avatarPath) {
    this.avatarPath = avatarPath;
  }

  public List<Cart> getCarts() {
    return carts;
  }

  public void setCarts(List<Cart> carts) {
    this.carts = carts;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

}
