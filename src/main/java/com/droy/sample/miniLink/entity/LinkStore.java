package com.droy.sample.miniLink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Entity class to bind with the corresponding table in the datastore.
 */
@Entity
@Table(name = "link_store", schema = "minilinkDB")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkStore {

    @Id
    @Column(name = "actual_link")
    private String actualLink;

    @Column(name = "mini_link")
    private String miniLink;

}
