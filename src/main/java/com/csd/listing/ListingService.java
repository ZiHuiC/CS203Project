package com.csd.listing;

import java.util.List;

public interface ListingService {
    List<Listing> listListings();
    Listing getListing(Long id);
    Listing addListing(Listing listing);
    Listing updateListing(Long id, Listing listing);
    void deleteListing(Long id);
}


//package csd.week6.book;
//
//import java.util.List;
//
//public interface BookService {
//    List<Book> listBooks();
//    Book getBook(Long id);
//    Book addBook(Book book);
//    Book updateBook(Long id, Book book);
//
//    /**
//     * Change method's signature: do not return a value for delete operation
//     * @param id
//     */
//    void deleteBook(Long id);
//}