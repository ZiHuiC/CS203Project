package com.csd.listing;

import com.csd.listing.exceptions.ListingNotFoundException;
import com.csd.listing.tag.TagNotFoundException;
import com.csd.listing.tag.TagRepository;
import com.csd.user.User;
import com.csd.user.exceptions.UserNotFoundException;
import com.csd.user.exceptions.UserNotMatchedException;
import com.csd.user.UserServiceImpl;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

    @GetMapping("/listingpage/mylistings")
    public List<ListingDTO> findListingByUser(@RequestParam Long userId) {
        User user = userService.getUser(userId);
        return listings.findByLister(user).stream().map(ListingDTO::new).toList();
    }

    @GetMapping("/listingpage")
    public List<ListingDTO> findListingByTitle(
        @RequestParam(required = true) String tag, 
        @RequestParam(required = true) String commitment, 
        @RequestParam(required = true) String username,  
        @RequestParam(required = true) String location, 
        @RequestParam(required = false) String... inName) {
        
        FilterDTO filters = new FilterDTO(commitment, tag, username, location);
            System.out.println(filters.getCommitment()+ filters.getLocation());
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
    @PostMapping(value="/listingpage/newlisting/imageupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageModel saveImage(@RequestParam Long id,
                                 @RequestParam (value="image") MultipartFile image) throws IOException {
        ImageModel img = new ImageModel(image.getOriginalFilename(), image.getContentType(),
                image.getBytes());
        listings.getReferenceById(id).setPhoto(img);
        return images.save(img);
    }

    @GetMapping("/listingpage/{id}")
    public ListingDTO findListingById(@PathVariable Long id) {
        if (listings.findListingById(id).isEmpty())
            throw new ListingNotFoundException(id);

        return new ListingDTO(listings.findListingById(id).get());
    }

    @GetMapping(value = "/listingpage/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getListingImage(@PathVariable Long id) throws IOException {
        if (listings.findListingById(id).isEmpty())
            throw new ListingNotFoundException(id);

        return listings.findListingById(id).get().getPhoto().getPicByte();
    }


    @PutMapping("/listingpage/edit/{id}")
    public ListingDTO updateListingById(@PathVariable Long id, 
            @Valid @RequestBody ListingChangeDTO newListingDetails
            ) {
        Optional<Listing> searchedListing = listings.findListingById(id);
        if (searchedListing.isEmpty())
            throw new ListingNotFoundException(id);
        Listing oldListing = searchedListing.get();
        String authName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userService.getUser(authName).getId() == oldListing.getLister().getId()
                || authName.compareTo("admin@lendahand.com") == 0){
            oldListing.setName(newListingDetails.getName());
            oldListing.setDes(newListingDetails.getDes());
            oldListing.setCommitment(newListingDetails.getCommitment());
            oldListing.setPhoto(newListingDetails.getPhoto());
            oldListing.setLocation(newListingDetails.getLocation());
            oldListing.setNoOfParticipants(newListingDetails.getNoOfParticipants());

            return tags.findTagByValue(newListingDetails.getTag()).map(tag -> {
                oldListing.setTag(tag);
                return new ListingDTO(listings.save(oldListing));
            }).orElseThrow(()->new TagNotFoundException(newListingDetails.getTag()));
        }
        throw new UserNotMatchedException(oldListing.getLister().getId());
    }

    @DeleteMapping("/listingpage/removal/{id}")
    public void deleteListing(@PathVariable Long id){
        Optional<Listing> searchedListing = listings.findListingById(id);
        if (searchedListing.isEmpty())
            throw new ListingNotFoundException(id);
        Listing listing = searchedListing.get();
        String authName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userService.getUser(authName).getId() == listing.getLister().getId()
                || authName.compareTo("admin@lendahand.com") == 0)
            listings.deleteById(id);
        else 
            throw new UserNotMatchedException(listing.getLister().getId());
    }
}
