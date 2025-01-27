package com.droy.sample.miniLink.controller;

import com.droy.sample.miniLink.entity.LinkStore;
import com.droy.sample.miniLink.exception.DataNotFoundException;
import com.droy.sample.miniLink.service.LinkConverterService;
import com.droy.sample.miniLink.utils.ConstantsHelper;
import com.droy.sample.miniLink.utils.LinkValidator;
import com.droy.sample.miniLink.vo.MiniLinkRequest;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.InvalidUrlException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * Rest Controller Class: Gateway to connect and serve the incoming requests
 * and respond back to the clients
 */
@RestController
@RequestMapping("/post")
public class MiniLinkController {
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(MiniLinkController.class);

    //Bucket to define the bandwidth limits to restricts the incoming API's
    private final Bucket bucket;

    //Service class which holds the logic to convert the long URL to Minilink URL
    @Autowired
    LinkConverterService linkConverterService;

    //Constructor
    public MiniLinkController(){
        //Creates the initial bandwidth for the API's to respond.
        //Creating the capacity of 5 and then refill with 5 more tokens
        // if there are <5 available after each minute.
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * This method is called to create the Minilink URLS for the given long urls.
     * This method also validates the incoming URL and also limits the users based on the bandwith limits.
     *
     * @param miniLinkRequest
     * @param request
     * @return
     * @throws Exception
     */
    @CrossOrigin("*")
    @PostMapping(value = "/create", consumes = {"application/json"})
    public LinkStore shortenUrl(@RequestBody final MiniLinkRequest miniLinkRequest, HttpServletRequest request) throws Exception {
        if (this.bucket.tryConsume(1)) {
            LOGGER.info("Received long url : " + miniLinkRequest.getActualLink());
            String longUrl = miniLinkRequest.getActualLink();
            //Validates the URL before processing the request
            if (LinkValidator.INSTANCE.validateURL(longUrl)) {
                //Read the local domain URL
                String localURL = request.getRequestURL().toString();
                LOGGER.info("local URL {}", localURL);
                //Send the local domain URL and the requested Long URL
                LinkStore miniLink = linkConverterService.miniLink(localURL, miniLinkRequest.getActualLink());
                LOGGER.info("New Mini url to: " + miniLink);
                return miniLink;
            }
            //If the URL is invalid throw the error
            throw new InvalidUrlException("Please enter a Valid URL");
        }
        //If the bandwidth is exhausted throw the error
        throw new TooManyListenersException("Too Many Requests");
    }

    /**
     * This API is used to get the long or actual URL for a given minilink.
     * This method throws error when there is data not found for the given minilink.
     * @param id
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws Exception
     */
    @CrossOrigin("*")
    @GetMapping(value = "/{id}")
    public RedirectView redirectUrl(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException, Exception {
        LOGGER.info("Received shortened url to redirect: " + id);
        try{
            //Get the actual URL from the datastore.
            String redirectUrlString = linkConverterService.getLongLinkFromID(ConstantsHelper._BASE_URL+id);
            LOGGER.info("Original URL: " + redirectUrlString);
            //Use this object to create a redirect view for the long url retrieved.
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(redirectUrlString);
            return redirectView;
        }catch (Exception e)
        {
            throw new DataNotFoundException(e.getMessage());
        }
    }

    /**
     * This API is used to get all the records from the datastore.
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws Exception
     */
    @CrossOrigin("*")
    @GetMapping(value = "/All")
    public List getAll() throws IOException, URISyntaxException, Exception {
        return linkConverterService.getAll();
    }
}
