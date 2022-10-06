package com.csd.listing;

import com.csd.application.Application;
import com.csd.application.ApplicationDTO;
import com.csd.user.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ListingDTO {
    private Long id;
    private String name;
    private String des;
    private Integer noOfParticipants;
    private UserDTO lister;
    private List<ApplicationDTO> applications;

    public ListingDTO(Listing listing) {
        this.id = listing.getId();
        this.name = listing.getName();
        this.des = listing.getDes();
        this.noOfParticipants = listing.getNoOfParticipants();
        this.lister = new UserDTO(listing.getLister());
        if (listing.getApplications() == null)
            this.applications = new ArrayList<>();
        else
            this.applications = listing.getApplications().stream().map(ApplicationDTO::new).collect(Collectors.toList());
    }
}
