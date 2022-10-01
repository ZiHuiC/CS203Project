package com.csd.csd;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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

    @NotNull
    @NotBlank
    private String des;

    @NotNull
    private Integer noOfParticipants;
}
