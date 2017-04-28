/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.common;

public class URLify {
    public synchronized static String buildURL(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }
}
