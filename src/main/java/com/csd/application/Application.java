package com.csd.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    @JoinColumn(name = "applicant_uuid", nullable = false)
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;
}
