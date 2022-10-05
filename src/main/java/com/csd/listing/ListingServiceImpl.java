//package com.csd.listing;
//
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ListingServiceImpl implements ListingService {
//    private ListingRepository listings;
//    public ListingServiceImpl(ListingRepository listings) {
//        this.listings = listings;
//    }
//
//    @Override
//    public List<Listing> listListings(){
//        return listings.findAll();
//    }
//
//    @Override
//    public Listing getListing(Long id) {
//        return listings.findListingById(id).orElse(null);
//    }
//
//    @Override
//    public Listing addListing(Listing listing) {
//        if (listing)
//        return listings.save(listing);
//    }
//
//    @Override
//    public Listing updateListing(Long id, Listing listing) {
//
//    }
//
//    @Override
//    public void deleteListing(Long id) {
//
//    }
//
//}
