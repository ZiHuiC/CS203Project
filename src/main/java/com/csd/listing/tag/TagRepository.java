package com.csd.listing.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findTagByValue(String value);
    @Transactional
    void deleteByValue(String value);
}
