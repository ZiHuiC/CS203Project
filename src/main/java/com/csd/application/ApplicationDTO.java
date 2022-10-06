package com.csd.application;

import com.csd.listing.ListingDTO;
import com.csd.user.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDTO {
    private Long id;
    private String message;
    private UserDTO applicant;

    public ApplicationDTO(Application application) {
        this.id = application.getId();
        this.message = application.getMessage();
        this.applicant = new UserDTO(application.getApplicant());
    }

}
