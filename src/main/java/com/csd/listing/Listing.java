package com.csd.listing;

import com.csd.application.Application;
import com.csd.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity // This tells Hibernate to make a table out of this class
public class Listing {
    @Id
    @Column(name="id", updatable=false, nullable = false)
    private Integer id;

    @NotNull
    @NotBlank
    private String name;
    private String des;
    private Integer noOfParticipants;

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User lister;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    public Listing(String name, String des) {
        this.name = name;
        this.des = des;
    }

}
