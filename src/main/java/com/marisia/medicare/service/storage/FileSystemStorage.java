package com.marisia.medicare.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public class FileSystemStorage implements StorageService {

  private final Path rootLocation;

  // @Autowired
  public FileSystemStorage(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }

  @Override
  public void store(MultipartFile file, String name) {
    try {
      if (file.isEmpty()) {
        throw new StorageException("Cannot store empty file");
      }

      var destinationFile = rootLocation.resolve(Paths.get(name)).normalize().toAbsolutePath();
      if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
        throw new StorageException("Cannot store file outside of configured directory");
      }

      try (InputStream is = file.getInputStream()) {
        Files.copy(is, destinationFile, StandardCopyOption.REPLACE_EXISTING);
      }

    } catch (Exception e) {
      throw new StorageException("Failed to store file.", e);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files
          .walk(rootLocation, 1)
          .filter(path -> !path.equals(rootLocation))
          .map(rootLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }
  }

  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      var file = load(filename);
      var resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);
      }

    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

}
