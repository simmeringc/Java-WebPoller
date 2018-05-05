/**
 * Created by simmeringc on 4/28/17.
 *
 * callable uses JSoup to preform an HTML get() on a website
 */

package com.simmeringc.websitePoller.controllers;

import static com.simmeringc.websitePoller.views.SystemLog.systemLogHtmlGetFailed;

import java.util.concurrent.Callable;
import org.jsoup.*;

public class WebRequester implements Callable {
    public String html = "";
    public String url;

    public WebRequester(String url) {
        this.url = url;
    }

    public String call() throws Exception {
        try {
            html = Jsoup.connect(url).get().html();
            if (html.toString().equals("")) {
                systemLogHtmlGetFailed(url);
            }
        } catch (Exception ex) {
            throw new Exception("HTML get failed");
        }
        return html;
    }
}

