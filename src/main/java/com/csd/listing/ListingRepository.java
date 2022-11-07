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
    Optional<Listing> findByTag(Tag tag);

    List<Listing> findListingByCommitment(String commitment);
    List<Listing> findByLister(User user);

    List<Listing> findByListerAndTag(User user, Tag tag);
    List<Listing> findByListerAndCommitment(User user, String commitment);
    List<Listing> findByCommitmentAndTag(String commitment, Tag tags);

    List<Listing> findByListerAndCommitmentAndTag(User user, String commitment, Tag tag);
    @Transactional
    void deleteByName(String name);

}