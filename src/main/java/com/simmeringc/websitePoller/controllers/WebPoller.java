/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.controllers;

import static com.simmeringc.websitePoller.controllers.WebRequester.getHtml;
import static com.simmeringc.websitePoller.controllers.LetterPairSimilarity.compareStrings;
import static com.simmeringc.websitePoller.views.SystemLog.systemLogHtmlGetFailed;

import java.text.DecimalFormat;

public class WebPoller implements Runnable {
    private String url;
    private String oldHtml;
    private String newHtml;
    private double thresholdPercent;
    private double percentDiff;
    private double percentSimilarity;

    public WebPoller(String url, String oldHtml, double thresholdPercent) {
        this.url = url;
        this.oldHtml = oldHtml;
        this.thresholdPercent = thresholdPercent;
    }

    public void run() {
        try {
            newHtml = getHtml(url);
            percentSimilarity = compareStrings(oldHtml, newHtml);
            setPercentDiff(percentSimilarity);
        } catch (Exception ex) {
            systemLogHtmlGetFailed(url);
            ex.printStackTrace();
        }
    }

    public double setPercentDiff(double percentSimilarity) {
        if (percentSimilarity == 1.0) {
            return 0.00;
        } else {
            double d = (1.00 - percentSimilarity);
            DecimalFormat df = new DecimalFormat("#.##");
            percentDiff = Double.parseDouble(df.format(d));
            return percentDiff;
        }
    }

    public double getPercentDiff() {
        return percentDiff;
    }

    public double getPercentSimilarity() {
        return percentSimilarity;
    }
}
