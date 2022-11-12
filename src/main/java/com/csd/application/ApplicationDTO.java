package com.csd.application;

import com.csd.user.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDTO {
    private Long id;
    private Long listingId;
    private String message;
    private UserDTO applicant;
    private String listingName;
    private String listingDes;

    public ApplicationDTO(Application application) {
        this.id = application.getId();
        this.message = application.getMessage();
        this.applicant = new UserDTO(application.getApplicant());
        this.listingId = application.getListing().getId();
        this.listingName = application.getListing().getName();
        this.listingDes = application.getListing().getDes();
    }

}
