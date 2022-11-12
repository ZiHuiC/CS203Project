package com.csd.application;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>{
    
    Optional<Application> findApplicationById(Long id);
    List<Application> findApplicationByMessage(String message);
}
