package com.csd.listing;

import com.csd.application.ApplicationDTO;
import com.csd.listing.tag.Tag;
import com.csd.listing.tag.TagDTO;
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
    private ImageModel photo;
    private String commitment;
    @ElementCollection
    private List<TagDTO> tags = new ArrayList<>();


    public ListingDTO(Listing listing) {
        this.id = listing.getId();
        this.name = listing.getName();
        this.des = listing.getDes();
        this.noOfParticipants = listing.getNoOfParticipants();
        this.lister = new UserDTO(listing.getLister());
        this.photo = listing.getPhoto();
        this.commitment = listing.getCommitment();
        if (listing.getTags() == null)
            this.tags = new ArrayList<>();
        else
            this.tags = listing.getTags().stream().map(TagDTO::new).collect(Collectors.toList());
        if (listing.getApplications() == null)
            this.applications = new ArrayList<>();
        else
            this.applications = listing.getApplications().stream().map(ApplicationDTO::new).collect(Collectors.toList());
    }
}
