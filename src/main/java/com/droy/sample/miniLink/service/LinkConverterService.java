package com.droy.sample.miniLink.service;

import com.droy.sample.miniLink.entity.LinkStore;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Link Converter Service Interface
 */
@Validated
public interface LinkConverterService {

    List<LinkStore> getAll() throws Exception;

    String getLongLinkFromID(String uniqueID) throws Exception;

    LinkStore miniLink(String localLink, String longLink) throws Exception;
}
