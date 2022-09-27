package com.csd.csd;

import org.springframework.data.repository.CrudRepository;

public interface ListingRepository extends CrudRepository<Listing, Integer> {

    Listing findListingById(Integer id);
}