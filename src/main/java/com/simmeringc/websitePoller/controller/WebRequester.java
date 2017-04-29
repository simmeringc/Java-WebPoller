/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.controller;

import static com.simmeringc.websitePoller.view.SystemLog.systemLogHtmlGetFailed;

import org.jsoup.*;



public class WebRequester {
    static String html = "";

    public static String getHtml(String url) throws Exception {
        try {
            html = Jsoup.connect(url).get().html();
            System.out.println(html);
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

