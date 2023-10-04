package com.marisia.medicare.service.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  void init();

  void store(MultipartFile file, String name);

  Stream<Path> loadAll();

  Path load(String filename);

  Resource loadAsResource(String filename);
}
