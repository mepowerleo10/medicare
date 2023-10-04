package com.marisia.medicare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.marisia.medicare.model.AppUser;
import com.marisia.medicare.service.AppUserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
  @Autowired
  AppUserService userService;

  @GetMapping("/get-profile")
  public String profile(Model model) {
    model.addAttribute("user", userService.getCurrentUser());
    return "users/_profile";
  }

  @GetMapping("/edit-profile")
  public String edit(Model model) {
    model.addAttribute("user", userService.getCurrentUser());
    return "users/_edit";
  }

  @PostMapping("/edit-profile")
  public String update(
      Model model,
      @Valid @ModelAttribute("user") AppUser data,
      @RequestParam(name = "updateAvatar", defaultValue = "false") Boolean updateAvatar,
      @RequestParam(name = "avatar", required = false) MultipartFile avatar,
      BindingResult result) {

    boolean hasErrors = false;
    var user = userService.getCurrentUser();

    if (userService.usernameAlreadyExists(user.getId(), data.getUsername())) {
      result.rejectValue("username", "error.user", "Username is already taken");
      hasErrors = true;
    }

    if (result.hasErrors()) {
      hasErrors = true;
    }

    if (hasErrors) {
      return "users/_edit";
    }

    if (data.getUsername() != null)
      user.setUsername(data.getUsername());

    if (updateAvatar) {
      user.setAvatarPath(data.getAvatarPath());
    }

    user.setRoles(user.getRoles());
    user.setAccount(data.getAccount());
    userService.save(user);

    return "redirect:/users/get-profile";
  }

}
