package com.marisia.medicare.model;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements UserDetails {
  private AppUser user;

  public SecurityUser(AppUser user) {
    this.user = user;
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  static String appendRole(String value) {
    return "ROLE_" + value;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays
        .stream(
            user
                .getRoles()
                .split(","))
        .map((SecurityUser::appendRole))
        .map(SimpleGrantedAuthority::new).toList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public AppUser getUser() {
    return user;
  }

  public void setUser(AppUser user) {
    this.user = user;
  }

}
