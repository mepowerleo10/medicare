package com.marisia.medicare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.marisia.medicare.model.SecurityUser;
import com.marisia.medicare.service.storage.StorageFileNotFoundException;
import com.marisia.medicare.service.storage.StorageService;

@Controller
@RequestMapping("/files")
public class StorageController {
  @Autowired
  private final StorageService storageService;

  public StorageController(StorageService storageService) {
    this.storageService = storageService;
  }

  @GetMapping("/avatars/user")
  @ResponseBody
  public ResponseEntity<Resource> serveCurrentUserAvatar() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    String path = null;

    if (authentication.isAuthenticated()) {
      var user = ((SecurityUser) authentication.getPrincipal()).getUser();
      path = user.getAvatarPath() != null ? user.getAvatarPath() : "default-avatar.png";
    }

    var file = storageService.loadAsResource(path);
    return ResponseEntity
        .ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachement; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }

  @GetMapping("/avatars/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveAvatar(@PathVariable String filename) {
    var file = storageService.loadAsResource(filename);
    return ResponseEntity
        .ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachement; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }

  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exception) {
    return ResponseEntity.notFound().build();
  }
}