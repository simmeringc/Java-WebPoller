/**
 * Created by Conner on 4/28/17.
 *
 * uses JSoup to preform an HTML get() on a website,
 * doesn't always work
 */

package com.simmeringc.websitePoller.controllers;

import static com.simmeringc.websitePoller.views.SystemLog.systemLogHtmlGetFailed;

import org.jsoup.*;

public class WebRequester {
    static String html = "";

    public static String getHtml(String url) throws Exception {
        try {
            html = Jsoup.connect(url).get().html();
            if (html.toString().equals("")) {
                systemLogHtmlGetFailed(url);
            }
        }
        catch (Exception ex) {
            throw new Exception("HTML get failed");
        }
        return html;
    }
}

