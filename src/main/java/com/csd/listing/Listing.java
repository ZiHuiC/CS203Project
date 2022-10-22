package com.csd.listing;

import com.csd.application.Application;
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

    // We define commitment length by Ad-Hoc / 1 Week / 1 Month / 3 Months / 6 Months / 1 Year / Long-term
    //                                0      / 1      / 4       / 12       / 24       / 48     / 50
//    private Integer commitmentLength;

    // Category tags, list containing related keywords
//    private List<String> categoryTags;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User lister;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    public Listing(String name, String des) {
        this.name = name;
        this.des = des;
    }

}
