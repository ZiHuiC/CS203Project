package com.csd.listing;

import com.csd.application.ApplicationDTO;
import com.csd.listing.tag.Tag;
import com.csd.user.UserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
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
    private String commitment;
    @ElementCollection
    private List<Tag> tags = new ArrayList<>();


    public ListingDTO(Listing listing) {
        this.id = listing.getId();
        this.name = listing.getName();
        this.des = listing.getDes();
        this.noOfParticipants = listing.getNoOfParticipants();
        this.lister = new UserDTO(listing.getLister());
        this.commitment = listing.getCommitment();
        this.tags = listing.getTags();
        if (listing.getApplications() == null)
            this.applications = new ArrayList<>();
        else
            this.applications = listing.getApplications().stream().map(ApplicationDTO::new).collect(Collectors.toList());
    }
}
