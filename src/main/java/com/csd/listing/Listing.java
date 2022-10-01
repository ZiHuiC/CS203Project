package com.csd.listing;

import com.csd.application.Application;
import com.csd.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;
// import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity // This tells Hibernate to make a table out of this class
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable=false, nullable = false)
    private Integer id;

    @NotNull
    private String name;
    private String des;
    private Integer noOfParticipants = -1; // -1 means unlimited

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User lister;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    public Listing(String name, String des, Integer noOfParticipants) {
        this.name = name;
        this.des = des;
        this.noOfParticipants = noOfParticipants;
    }


}
