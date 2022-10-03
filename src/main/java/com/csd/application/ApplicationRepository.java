package com.csd.application;

import java.util.Optional;

import com.csd.listing.Listing;
import com.csd.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>{
    
    Optional<Application> findApplicationById(Long id);
    Iterable<Application> findAllByListing(Listing listing);
    Iterable<Application> findAllByApplicant(User applicant);
}
