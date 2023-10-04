package com.marisia.medicare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.marisia.medicare.model.AppUser;
import com.marisia.medicare.model.SecurityUser;
import com.marisia.medicare.repository.AppUserRepository;

@Service
public class AppUserService implements UserDetailsService {
  @Autowired
  private AppUserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .map(SecurityUser::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }

  public boolean usernameAlreadyExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  public boolean usernameAlreadyExists(Integer id, String username) {
    return userRepository.findByUsername(username).get().getId() != id;
  }

  public AppUser registerUser(AppUser user) {
    return userRepository.save(user);
  }

  public AppUser getCurrentUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }

    if (authentication.getPrincipal() instanceof SecurityUser) {
      var securityUser = (SecurityUser) authentication.getPrincipal();
      return securityUser.getUser();
    }

    return null;
  }

  public AppUser getSystemAsUser() {
    return userRepository.findById(1).get();
  }

  public Iterable<AppUser> getAllCustomers() {
    return userRepository.findAllCustomers();
  }

  public AppUser save(AppUser user) {
    return userRepository.save(user);
  }

}
