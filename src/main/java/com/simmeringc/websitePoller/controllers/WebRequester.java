/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.controllers;

import static com.simmeringc.websitePoller.views.SystemLog.systemLogHtmlGetFailed;
import com.simmeringc.websitePoller.models.Node;

import org.jsoup.*;



public class WebRequester {
    static String html = "";

    public static String getHtml(String url) throws Exception {
        try {
            html = Jsoup.connect(url).get().html();
            if (html.toString().equals("")) {
                systemLogHtmlGetFailed(url);
            }
            Node page = new Node(html);
        }
        catch (Exception ex) {
            throw new Exception("HTML get failed");
        }
        return html;
    }
}

