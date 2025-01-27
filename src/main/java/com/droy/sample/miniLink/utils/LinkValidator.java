package com.droy.sample.miniLink.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Help class to Validate the incoming long URL with the given pattern.
 */
public class LinkValidator {
    public static final LinkValidator INSTANCE = new LinkValidator();
    private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private LinkValidator() {
    }

    /**
     * Mathod to validate the incoming url from the request.
     * @param url
     * @return
     */
    public boolean validateURL(String url) {
        Matcher m = URL_PATTERN.matcher(url);
        return m.matches();
    }
}
