package com.csd.listing;

import com.csd.application.Application;
import com.csd.listing.tag.Tag;
import com.csd.listing.tag.TagRepository;
import com.csd.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;
// import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity // This tells Hibernate to make a table out of this class
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable=false, nullable = false)
    private Long id;

    @NotNull
    private String name;
    private String des;
    private Integer noOfParticipants = -1; // -1 means unlimited
    /*
    adhoc
    1week
    1month
    3months
    6months
    1year
    long-term
     */
    private String commitment;

    private String location;

    @ManyToOne
    @JoinColumn(name = "Tag", nullable = false)
    private Tag tag;

    @OneToOne
    private ImageModel photo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User lister;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    public Listing(String name, String des, String commitment, String location) {
        this.name = name;
        this.des = des;
        this.commitment = commitment;
        this.location = location;
    }

    public Listing(String name, String des, String commitment, String location, int noOfParticipants) {
        this.name = name;
        this.des = des;
        this.commitment = commitment;
        this.location = location;
        this.noOfParticipants = noOfParticipants;
    }

}
