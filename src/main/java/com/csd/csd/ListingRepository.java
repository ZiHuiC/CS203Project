package com.csd.csd;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, UUID> {

    Listing findListingById(Integer id);
}