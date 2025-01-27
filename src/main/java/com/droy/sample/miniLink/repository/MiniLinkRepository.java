package com.droy.sample.miniLink.repository;

import com.droy.sample.miniLink.entity.LinkStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository Class to bind the VO to Datastore.
 */
public interface MiniLinkRepository extends JpaRepository<LinkStore, String> {

    /**
     * This custom method will fetch the given minilink data from the datastore.
     * @param miniLink
     * @return
     */
    @Query("SELECT l FROM LinkStore l WHERE l.miniLink = :miniLink")
    LinkStore findByMiniLink(@Param("miniLink") String miniLink);
}
