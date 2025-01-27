package com.droy.sample.miniLink.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request JSON Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniLinkRequest {
    private String id;
    private String actualLink;
    private String minilink;
}
