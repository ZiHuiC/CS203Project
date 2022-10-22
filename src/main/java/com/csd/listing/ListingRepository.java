package com.csd.listing;

import com.csd.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    Optional<Listing> findListingById(Long id);
    List<User> findAll(ListingSpecification spec);
//    Optional<Listing> findListingByCommitmentLength(String commitmentLength);
//    Optional<Listing> findListingByCategoryTags(String... tag);
//    Optional<Listing> findListingByCommitmentAndByTags(String commitmentLength, String... tag);
}