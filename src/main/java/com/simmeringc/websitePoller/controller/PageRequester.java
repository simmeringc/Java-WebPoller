/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.controller;

import org.jsoup.*;

public class PageRequester {
    static String html;

    public synchronized static String getHTML(String url) throws Exception {
        try {
            html = Jsoup.connect(url).get().html();
            if (html.toString().equals("")) {
                throw new Exception("HTML get failed");
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return html;
    }
}

