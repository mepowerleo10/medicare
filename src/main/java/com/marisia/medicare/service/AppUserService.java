package com.marisia.medicare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marisia.medicare.model.AppUser;
import com.marisia.medicare.model.SecurityUser;
import com.marisia.medicare.repository.AppUserRepository;
import com.marisia.medicare.service.payment.PaymentProperties;

@Service
public class AppUserService implements UserDetailsService {
  @Autowired
  private AppUserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository
        .findByUsername(username)
        .map(SecurityUser::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }

  public boolean usernameAlreadyExists(String username) {
    return repository.findByUsername(username).isPresent();
  }

  public boolean usernameAlreadyExists(Integer id, String username) {
    return repository.findByUsername(username).get().getId() != id;
  }

  public AppUser registerUser(AppUser user) {
    return repository.save(user);
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
    return repository.findById(1).get();
  }

  public Iterable<AppUser> getAllCustomers() {
    return repository.findAllCustomers();
  }

  public AppUser save(AppUser user) {
    return repository.save(user);
  }

  public void initializeSystemAndAdminUsers(PaymentProperties paymentProperties, PasswordEncoder encoder) {
    var superUsers = repository.findAllById(List.of(1, 2));
    if (!superUsers.iterator().hasNext()) {
      save(new AppUser(1, "system", encoder.encode("none"), paymentProperties.getAccountNumber(), "SYSTEM"));
      save(new AppUser(2, "admin", encoder.encode("welcome"), null, "USER,ADMIN"));
    }
  }

}
