package com.csd.csd;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Integer> {

    Listing findListingById(Integer id);
}