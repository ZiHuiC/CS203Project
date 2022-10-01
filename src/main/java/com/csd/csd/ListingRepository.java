package com.csd.csd;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ListingRepository extends CrudRepository<Listing, Integer> {

    Optional<Listing> findListingById(Integer id);
}