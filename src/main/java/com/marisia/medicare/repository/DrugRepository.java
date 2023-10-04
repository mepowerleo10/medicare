package com.marisia.medicare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marisia.medicare.model.Drug;


public interface DrugRepository extends JpaRepository<Drug, Long> {

  List<Drug> findByEnabled(boolean enabled);

  Optional<Drug> findByName(String name);
  
}
