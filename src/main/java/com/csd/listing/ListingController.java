package com.csd.listing;

import com.csd.listing.tag.Tag;
import com.csd.listing.tag.TagNotFoundException;
import com.csd.listing.tag.TagRepository;
import com.csd.user.User;
import com.csd.user.UserDTOs.UserDTO;
import com.csd.user.UserNotFoundException;
import com.csd.user.UserRepository;
import com.csd.user.UserServiceImpl;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Filter;
import java.util.stream.Collectors;

import static com.csd.listing.util.Util.compressBytes;

@CrossOrigin
@RestController
public class ListingController {
    private final ListingRepository listings;
    private final UserRepository users;
    private final ImageRepository images;
    private final TagRepository tags;
    private UserServiceImpl userService;

    public ListingController
            (ListingRepository listings,
             UserRepository users,
             ImageRepository images,
             TagRepository tags,
             UserServiceImpl userService) {
        this.listings = listings;
        this.users = users;
        this.images = images;
        this.tags = tags;
        this.userService = userService;
    }



    @GetMapping("/listingpage")
    public List<ListingDTO> findListingByTitle(@RequestBody(required = false) FilterDTO filters, @RequestParam String... inName) {
        if (filters == null && inName == null)
            return listings.findAll().stream().map(ListingDTO::new).collect(Collectors.toList());

        List<ListingDTO> result = new ArrayList<>();
        List<ListingDTO> filteredListings;

        if (filters != null) {
            filteredListings = new ArrayList<>();
            // Filtering of listings
            for (Listing l: listings.findAll()) {
                if (filters.isValid(l))
                    filteredListings.add(new ListingDTO(l));
            }
        } else
            filteredListings = listings.findAll().stream().map(ListingDTO::new).collect(Collectors.toList());

        // Check if inName is empty
        if (inName == null)
            return filteredListings;

        // See name
        for (ListingDTO l: filteredListings) {
            for (String item: inName) {
                if (l.getName().toLowerCase().contains(item.toLowerCase()))
                    result.add(l);
            }
        }

        return result;
    }

//    @GetMapping("/listingpage")
//    public List<ListingDTO> getListings
//            (@RequestParam(required = false) String commitment,
//            @RequestParam(required = false) String tag,
//            @RequestParam(required = false) String username) {
//
//        if (commitment != null && tag != null && username != null){
//            tags.findTagByValue(tag).map(tagVal -> {
//                    User userVal = userService.getUser(username);
//                    if (userVal == null)
//                        throw new UserNotFoundException(username);
//                    return listings.findByListerAndCommitmentAndTag(userVal,commitment, tagVal)
//                            .stream().map(ListingDTO::new).collect(Collectors.toList());
//            }).orElseThrow(() -> new TagNotFoundException(tag));
//        }
//        // trust that input commitment is correct
//        if (commitment != null && tag != null){
//            tags.findTagByValue(tag).map(tagVal -> {
//                    return listings.findByCommitmentAndTag(commitment, tagVal).stream().map(ListingDTO::new).collect(Collectors.toList());
//            }).orElseThrow(() -> new TagNotFoundException(tag));
//        }
//
//        if (username != null && tag != null){
//            tags.findTagByValue(tag).map(tagVal -> {
//                    User userVal = userService.getUser(username);
//                    if (userVal == null)
//                        throw new UserNotFoundException(username);
//                    return listings.findByListerAndTag(userVal, tagVal).stream().map(ListingDTO::new).collect(Collectors.toList());
//            }).orElseThrow(() -> new TagNotFoundException(tag));
//        }
//
//        if (username != null && commitment != null){
//            User userVal = userService.getUser(username);
//            if (userVal == null)
//                throw new UserNotFoundException(username);
//            return listings.findByListerAndCommitment(userVal, commitment).stream().map(ListingDTO::new).collect(Collectors.toList());
//
//        }
//        // trust that input commitment is correct
//        if (commitment != null)
//            return listings.findListingByCommitment(commitment)
//                .stream().map(ListingDTO::new).collect(Collectors.toList());
//        if (tag != null)
//            tags.findTagByValue(tag).map(tagVal -> {
//                    return listings.findByTag(tagVal).stream().map(ListingDTO::new).collect(Collectors.toList());
//            }).orElseThrow(() -> new TagNotFoundException(tag));
//        if (username != null){
//            User userVal = userService.getUser(username);
//            if (userVal == null)
//                throw new UserNotFoundException(username);
//            return listings.findByLister(userVal).stream().map(ListingDTO::new).collect(Collectors.toList());
//        }
//        // no param
//        return listings.findAll().stream().map(ListingDTO::new).collect(Collectors.toList());

//    }

    @PostMapping("/listingpage/createlisting")
    public ListingDTO addListing(@RequestParam Long userId, @RequestBody Listing listing) {
        // need to add the user_id to the table
        return users.findById(userId).map(user -> {
            listing.setLister(user);
//            listing.setTags(Arrays.asList("full time", "computer science"));
            return new ListingDTO(listings.save(listing));
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @PostMapping("/listingpage/createlisting/imageupload")
    public ImageModel saveUser(@RequestParam Long id,
                                 @RequestParam("image") MultipartFile multipartFile) throws IOException {
        ImageModel img = new ImageModel(multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                multipartFile.getBytes());
        listings.getReferenceById(id).setPhoto(img);
        return images.save(img);
    }


    @GetMapping("/listingpage/{id}")
    public ListingDTO findListingById(@PathVariable Long id) {
        if (listings.findListingById(id).isEmpty())
            throw new ListingNotFoundException(id);

        return new ListingDTO(listings.findListingById(id).get());
    }

}
