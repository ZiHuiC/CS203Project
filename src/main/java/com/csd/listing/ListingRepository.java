package com.csd.listing;

import com.csd.listing.tag.Tag;
import com.csd.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    Optional<Listing> findByName(String name);
    Optional<Listing> findListingById(Long id);
    List<Listing> findByLister(User user);
    @Transactional
    void deleteByName(String name);

}