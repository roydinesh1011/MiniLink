package com.droy.sample.miniLink.service;

import com.droy.sample.miniLink.entity.LinkStore;
import com.droy.sample.miniLink.repository.MiniLinkRepository;
import com.droy.sample.miniLink.utils.ConstantsHelper;
import com.droy.sample.miniLink.utils.LinkConverter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This Class holds most of the logic to convert the incoming long URL into a Minilink URL.
 */
@Service
@NoArgsConstructor
//@AllArgsConstructor
public class LinkConverterServiceImpl implements LinkConverterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkConverterService.class);

    // Repository to send the data to datastore
    @Autowired
    private MiniLinkRepository miniLinkRepository;

    /**
     * This method converts the incoming @param longlink into a minilink and returns the same to clients
     *
     * @param localLink
     * @param longLink
     * @return
     */
    @Override
    public LinkStore miniLink(String localLink, String longLink) throws Exception {
        String methodNname = "miniLink";
        LOGGER.info("In Method {} ", methodNname);
        // Generate a random UUID which is used as the code to generate the minilink URL
        Long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        LOGGER.info("Generated UUID {}", uuid);
        //Converting the UUID into a Unique ID using Base62
        String uniqueID = LinkConverter.INSTANCE.createUniqueLink(Long.valueOf(uuid));
        //Extract the base URL
        String baseString = formatLocalLinkFromMiniLink(localLink);
        //Append the base URL with the generated unique ID which form the minilink URL
        String miniLink = baseString + uniqueID;
        //Creating the VO
        LinkStore linkStore = new LinkStore(longLink, miniLink);
        //Persisting the VO in a persistant store in this case H2DB
        miniLinkRepository.save(linkStore);
        //Return the VO object
        return linkStore;
    }

    /**
     * This method is used for getting the data from the datastore when requested for a specified ID.
     * @param uniqueID
     * @return
     * @throws Exception
     */
    @Override
    public String getLongLinkFromID(String uniqueID) throws Exception {
        //Call the repository method to get the Long URL based on the the Minilink URL passed as a parameter.
        LinkStore linkStore = miniLinkRepository.findByMiniLink(uniqueID);
        if(linkStore != null) {
            LOGGER.info("Converting shortened URL back to {}", linkStore.getActualLink());
            return linkStore.getActualLink();
        }
        throw new Exception("Actual URL Not Found for the given MiniLink");
    }

    /**
     * This method will return all the long URL and minilink URL mappings from the datastore.
     * @return
     * @throws Exception
     */
    @Override
    public List<LinkStore> getAll() throws Exception {
        //Fetch all the records from the datastore
        List<LinkStore> linkStores = miniLinkRepository.findAll();
        LOGGER.info("Converting shortened URL back to {}", linkStores);
        return linkStores;
    }

    /**
     * This is a helper method to format and append the minilink unique code into an URL.
     * @param localURL
     * @return
     */
    private String formatLocalLinkFromMiniLink(String localURL) {
        String[] addressComponents = localURL.split("/");
        StringBuilder sb = new StringBuilder();
        //Loop through the array and format the URL appropriately
        for (int i = 0; i < addressComponents.length - 1; ++i) {
            sb.append(addressComponents[i]);
            //Check if the string is equal to http: then add //
            if(ConstantsHelper._HTTP.equals(sb.toString()))
            {
                sb.append("//");
            }
            //Check if the string is ending with 8080 then add /
            if(sb.toString().endsWith(ConstantsHelper._ENDS_WITH_8080))
            {
                sb.append("/");
            }
        }
        //finally append / for proper formatting of the URL
        sb.append('/');
        LOGGER.info("Base Link {}", sb.toString());
        return sb.toString();
    }
}
