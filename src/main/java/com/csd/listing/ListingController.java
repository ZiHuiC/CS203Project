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
    private final UserServiceImpl userService;

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

    /**
     * get a specific listing by username
     * @param userId
     * @return all the listings made by the user
     */
    @GetMapping("/listingpage/mylistings")
    public List<ListingDTO> findListingByUser(@RequestParam Long userId) {
        User user = userService.getUser(userId);
        return listings.findByLister(user).stream().map(ListingDTO::new).toList();
    }

    /**
     * check if a filter has all the parameters used
     * @param filter
     * @return true if the filter has all the parameters. false otherwise.
     */
    private static boolean isFilterByAll(FilterRequest filter){
        return FilterRequest.isAll(filter.getCommitment()) 
            && FilterRequest.isAll(filter.getLocation())
            && FilterRequest.isAll(filter.getUsername())
            && FilterRequest.isAll(filter.getTag());
    }

    /**
     * get a titles with matching tag, commitment, username, location and texts in title
     * @param tag, @param commitment, @param username, 
     * @param location, @param inName
     * @return List of ListingDTOs
     */
    @GetMapping("/listingpage")
    public List<ListingDTO> findListingByTitle(
        @RequestParam String tag,
        @RequestParam String commitment,
        @RequestParam String username,
        @RequestParam String location,
        @RequestParam(required = false) String... inName) {
        
        FilterRequest filters = new FilterRequest(commitment, tag, username, location);
        
        if (isFilterByAll(filters) && inName == null)
            return listings.findAll().stream().map(ListingDTO::new).collect(Collectors.toList());

        List<ListingDTO> result = new ArrayList<>();
        List<ListingDTO> filteredListings;

        if (!isFilterByAll(filters)) {
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

    /**
     * creates a listing object
     * @param userId
     * @param tagName
     * @param listing
     * @return the created listing in a ListingDTO
     */
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

    /**
     * uploads an image
     * @param id
     * @param image
     * @return the uploaded image
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value="/listingpage/newlisting/imageupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageModel saveImage(@RequestParam Long id,
                                 @RequestParam (value="image") MultipartFile image) throws IOException {
        ImageModel img = new ImageModel(image.getOriginalFilename(), image.getContentType(),
                image.getBytes());
        listings.getReferenceById(id).setPhoto(img);
        return images.save(img);
    }

    /**
     * get a specific listing by id
     * @param id
     * @return the specified ListingDTO
     */
    @GetMapping("/listingpage/{id}")
    public ListingDTO findListingById(@PathVariable Long id) {
        if (listings.findListingById(id).isEmpty())
            throw new ListingNotFoundException(id);

        return new ListingDTO(listings.findListingById(id).get());
    }

    /**
     * get the image used in the listing
     * @param id
     * @return the specified image bytearray from the listing
     */
    @GetMapping(value = "/listingpage/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getListingImage(@PathVariable Long id) throws IOException {
        if (listings.findListingById(id).isEmpty())
            throw new ListingNotFoundException(id);

        return listings.findListingById(id).get().getPhoto().getPicByte();
    }

    /**
     * updates a specified listing
     * @param id 
     * @param newListingDetails
     * @return updated ListingDTO
     */
    @PutMapping("/listingpage/edit/{id}")
    public ListingDTO updateListingById(@PathVariable Long id, 
            @Valid @RequestBody ListingChangeRequest newListingDetails
            ) {
        Optional<Listing> searchedListing = listings.findListingById(id);
        if (searchedListing.isEmpty())
            throw new ListingNotFoundException(id);
        Listing oldListing = searchedListing.get();

        if (userService.isUserOrAdmin(oldListing.getLister().getId())){
            oldListing.updateListing(newListingDetails);

            return tags.findTagByValue(newListingDetails.getTag()).map(tag -> {
                oldListing.setTag(tag);
                return new ListingDTO(listings.save(oldListing));
            }).orElseThrow(()->new TagNotFoundException(newListingDetails.getTag()));
        }
        throw new UserNotMatchedException(oldListing.getLister().getId());
    }

    /**
     * delete a specific listing by id
     * @param id
     */
    @DeleteMapping("/listingpage/removal/{id}")
    public void deleteListing(@PathVariable Long id){
        Optional<Listing> searchedListing = listings.findListingById(id);
        if (searchedListing.isEmpty())
            throw new ListingNotFoundException(id);
        Listing listing = searchedListing.get();

        if (userService.isUserOrAdmin(listing.getLister().getId()))
            listings.deleteById(id);
        else 
            throw new UserNotMatchedException(listing.getLister().getId());
    }

}
