/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.controller;

import static com.simmeringc.websitePoller.controller.WebRequester.getHtml;
import static com.simmeringc.websitePoller.view.SystemLog.systemLogHtmlGetFailed;

public class WebPoller implements Runnable {
    public String url;
    public int trackerNumber;
    public int thresholdPercent;
    public String newHtml;

    public WebPoller(String url, int trackerNumber, int thresholdPercent) {
        this.url = url;
        this.trackerNumber = trackerNumber;
        this.thresholdPercent = thresholdPercent;
    }

    public void run() {
        try {
            newHtml = getHtml(url);

        } catch (Exception ex) {
            systemLogHtmlGetFailed(url);
            ex.printStackTrace();
        }

    }
}
