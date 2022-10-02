package com.csd.listing;

import com.csd.application.Application;
import com.csd.user.User;
import com.csd.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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


    // please set nullable to false. Set to true now so that we can add listing without users
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
