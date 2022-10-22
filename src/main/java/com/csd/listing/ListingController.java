package com.csd.listing;

import com.csd.listing.tag.Tag;
import com.csd.listing.tag.TagRepository;
import com.csd.user.UserNotFoundException;
import com.csd.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class ListingController {
    private final ListingRepository listings;
    private final UserRepository users;
    private final TagRepository tags;

    public ListingController(ListingRepository listings, UserRepository users, TagRepository tags) {
        this.listings = listings;
        this.users = users;
        this.tags = tags;
    }

    @GetMapping("/listingpage")
    public List<ListingDTO> getListings
            (@RequestParam(required = false) String commitment, @RequestParam(required = false) List<String> tagsArg) {

        List<Tag> tagsList = new ArrayList<>();
        if (tagsArg != null)
            for (String t:tagsArg) {
                Optional<Tag> tag = tags.findTagByValue(t);
                tag.ifPresent(tagsList::add);
            }

       if (commitment != null && !tagsList.isEmpty())
           return listings.findByCommitmentAndTagsIn(commitment, tagsList).stream().map(ListingDTO::new).collect(Collectors.toList());
        if (commitment != null)
            return listings.findListingByCommitment(commitment)
                .stream().map(ListingDTO::new).collect(Collectors.toList());
        if (!tagsList .isEmpty())
           return listings.findByTagsIn(tagsList).stream().map(ListingDTO::new).collect(Collectors.toList());
        return listings.findAll().stream().map(ListingDTO::new).collect(Collectors.toList());

    }

    @PostMapping("/listingpage/createlisting")
    public ListingDTO addListing(@RequestParam Long userId, @RequestBody Listing listing) {
        // need to add the user_id to the table
        return users.findById(userId).map(user -> {
            listing.setLister(user);
//            listing.setTags(Arrays.asList("full time", "computer science"));
            return new ListingDTO(listings.save(listing));
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @GetMapping("/listingpage/{id}")
    public ListingDTO findListingById(@PathVariable Long id) {
        if (listings.findListingById(id).isEmpty())
            throw new ListingNotFoundException(id);
        return new ListingDTO(listings.findListingById(id).get());
    }

}
