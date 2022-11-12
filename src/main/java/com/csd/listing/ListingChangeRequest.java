package com.csd.listing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingChangeRequest {
    private String name;
    private String des;
    private Integer noOfParticipants;
    private ImageModel photo;
    private String commitment;
    private String location;
    private String tag;

    public ListingChangeRequest(
            String name,
            String des,
            Integer noOfParticipants,
            ImageModel photo,
            String commitment,
            String location,
            String tag
    ) {
        this.name = name;
        this.des = des;
        this.noOfParticipants = noOfParticipants;
        this.photo = photo;
        this.commitment = commitment;
        this.location = location;
        this.tag = tag;
    }
}
