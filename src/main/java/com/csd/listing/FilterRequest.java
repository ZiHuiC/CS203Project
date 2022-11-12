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
public class FilterRequest {
    private String commitment;
    private String tag;
    private String username;
    private String location;

    public Boolean isValid(Listing l) {
        return isCommitment(commitment, l) 
                && isTag(tag, l)
                && isUsername(username, l)
                && isLocation(location, l);
    }

    @Override
    public String toString() {
        return "FilterDTO{" +
                "commitment='" + commitment + '\'' +
                ", tag='" + tag + '\'' +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public static boolean isAll(String val) {
        return val.toLowerCase().equals("all");
    }

    private static boolean isCommitment(String commitment, Listing l) {
        return isAll(commitment) || Objects.equals(commitment, l.getCommitment());
    }

    private static boolean isTag(String tag, Listing l) {
        return isAll(tag) || Objects.equals(tag, l.getTag().getValue());
    }

    private static boolean isUsername(String username, Listing l) {
        return isAll(username) || Objects.equals(username, l.getLister().getUsername());
    }

    private static boolean isLocation(String location, Listing l) {
        return isAll(location) || Objects.equals(location, l.getLocation());
    }
}
