package com.csd.application;

import com.csd.user.UserDTOs.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDTO {
    private Long id;
    private Long listingId;
    private String message;
    private UserDTO applicant;

    public ApplicationDTO(Application application) {
        this.id = application.getId();
        this.message = application.getMessage();
        this.applicant = new UserDTO(application.getApplicant());
        this.listingId = application.getListing().getId();
    }

}
