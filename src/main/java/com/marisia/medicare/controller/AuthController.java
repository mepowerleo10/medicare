package com.marisia.medicare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.marisia.medicare.model.AppUser;
import com.marisia.medicare.service.AppUserService;
import com.marisia.medicare.service.storage.StorageException;
import com.marisia.medicare.service.storage.StorageService;

import jakarta.validation.Valid;

@Controller
public class AuthController {
  @Autowired
  private final AppUserService userService;

  @Autowired
  private final StorageService storageService;

  @Autowired
  private final PasswordEncoder encoder;

  public AuthController(AppUserService userService, PasswordEncoder encoder, StorageService storageService) {
    this.userService = userService;
    this.encoder = encoder;
    this.storageService = storageService;
  }

  @GetMapping("/login")
  public String login() {
    return "auth/login";
  }

  @GetMapping("/logout")
  public String logout() {
    return "auth/logout";
  }

  @GetMapping("/register")
  public ModelAndView registrationPage() {
    var mv = new ModelAndView("auth/register");
    mv.addObject("user", new AppUser());
    return mv;
  }

  @PostMapping("/register")
  public String register(
      @Valid @ModelAttribute("user") AppUser userData,
      @RequestParam("avatar") MultipartFile avatar,
      BindingResult result) {

    boolean hasErrors = false;

    if (userService.usernameAlreadyExists(userData.getUsername())) {
      result.rejectValue("username", "error.user", "Username is already taken");
      hasErrors = true;
    }

    if (result.hasErrors()) {
      hasErrors = true;
    }

    if (!userData.passwordsMatch()) {
      result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
      hasErrors = true;
    }

    if (hasErrors) {
      return "auth/register";
    }

    var newUser = new AppUser();
    newUser.setUsername(userData.getUsername());
    newUser.setRoles("USER");
    newUser.setPassword(encoder.encode(userData.getPassword()));

    if (!avatar.isEmpty()) {
      try {
        String newPath = String.format("%s-%d-%s", newUser.getUsername(), System.currentTimeMillis(),
            avatar.getOriginalFilename());
        storageService.store(avatar, newPath);
        newUser.setAvatarPath(newPath);
      } catch (StorageException e) {
        e.printStackTrace();
      }
    }

    userService.registerUser(newUser);

    return "redirect:/login";
  }
}
