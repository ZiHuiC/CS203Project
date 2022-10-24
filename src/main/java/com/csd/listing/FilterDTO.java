package com.csd.listing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterDTO {
    private String commitment;
    private String tag;
    private String username;
//    private String location;
    public Boolean isValid(Listing l) {
        return Objects.equals(commitment, l.getCommitment())
                && Objects.equals(tag, l.getTag().getValue())
                && Objects.equals(username, l.getLister().getUsername());
    }

    @Override
    public String toString() {
        return "FilterDTO{" +
                "commitment='" + commitment + '\'' +
                ", tag='" + tag + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
