package com.csd.listing;

import com.csd.listing.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    Optional<Listing> findListingById(Long id);
    List<Listing> findListingByCommitment(String commitment);

    List<Listing> findListingByTag(Tag tag);
//    List<Listing> retrieveByCommitmentFilterByTags(List<Tag> tags);

//    @Query("SELECT l FROM Listing l JOIN l.tags t WHERE l.commitment = LOWER(:commitment) AND t = LOWER(:tag)")
//    List<Listing> retrieveByCommitmentFilterByTag(@Param("commitment") String commitment, @Param("tag") String tag);


}