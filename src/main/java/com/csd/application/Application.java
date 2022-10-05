package com.csd.application;

import javax.persistence.*;

import com.csd.listing.Listing;
import com.csd.user.User;
import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String message;

//    @ManyToOne
//    @JoinColumn(name = "applicant_id", nullable = false)
//    private User applicant;
//
//    @ManyToOne
//    @JoinColumn(name = "listing_id", nullable = false)
//    private Listing listing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

//    @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private OffsetDateTime dateCreated;
//
//    @LastModifiedDate
//    @Column(nullable = false)
//    private OffsetDateTime lastUpdated;

    public Application(String message){
        this.message = message;
    }
}
