package com.csd.listing.tag;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDTO {
    private Long id;
    private String value;

    public TagDTO(Tag tag){
        this.id = tag.getId();
        this.value = tag.getValue();
    }
}
