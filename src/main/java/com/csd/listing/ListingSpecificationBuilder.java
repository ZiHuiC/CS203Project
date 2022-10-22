package com.csd.listing;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListingSpecificationBuilder {

    private final List<SearchCriteria> params;

    public ListingSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public ListingSpecificationBuilder with(String key, SearchOperation operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Listing> build() {
        if (params.size() == 0)
            return null;

        Specification<Listing> result = new ListingSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new ListingSpecification(params.get(i)))
                    : Specification.where(result).and(new ListingSpecification(params.get(i)));
        }

        return result;
    }
}