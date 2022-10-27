package com.csd.listing;

import com.csd.listing.exceptions.ListingNotFoundException;
import com.csd.listing.tag.Tag;
import com.csd.listing.tag.TagNotFoundException;
import com.csd.listing.tag.TagRepository;
import com.csd.user.User;
import com.csd.user.exceptions.UserNotFoundException;
import com.csd.user.UserRepository;
import com.csd.user.UserServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Filter;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class ListingController {
    private final ListingRepository listings;
    private final ImageRepository images;
    private final TagRepository tags;
    private UserServiceImpl userService;

    public ListingController
            (ListingRepository listings,
             ImageRepository images,
             TagRepository tags,
             UserServiceImpl userService) {
        this.listings = listings;
        this.images = images;
        this.tags = tags;
        this.userService = userService;
    }

    @GetMapping("/listingpage")
    public List<ListingDTO> findListingByTitle(@RequestBody(required = false) FilterDTO filters, @RequestParam(required = false) String... inName) {
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/listingpage/newlisting")
    public ListingDTO addListing(@RequestParam Long userId, 
                @RequestParam String tagName, 
                @RequestBody Listing listing) {
        // need to add the user_id to the table
        User user = userService.getUser(userId);
        if (user == null)
            throw new UserNotFoundException(userId);
        
        listing.setLister(user);
        return tags.findTagByValue(tagName).map(tag -> {
            listing.setTag(tag);
            return new ListingDTO(listings.save(listing));
        }).orElseThrow(()->new TagNotFoundException(tagName));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/listingpage/newlisting/imageupload")
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
