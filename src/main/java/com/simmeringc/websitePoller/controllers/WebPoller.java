/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.controllers;

import com.simmeringc.websitePoller.views.TrackerTile;

import static com.simmeringc.websitePoller.controllers.GoogleMail.sendMail;
import static com.simmeringc.websitePoller.controllers.WebRequester.getHtml;
import static com.simmeringc.websitePoller.controllers.LetterPairSimilarity.compareStrings;
import static com.simmeringc.websitePoller.views.MainWindow.trackerTiles;
import static com.simmeringc.websitePoller.views.SystemLog.systemLogHtmlGetFailed;

import java.text.DecimalFormat;

public class WebPoller implements Runnable {
    private int trackerNumber;
    private TrackerTile trackerTile;
    private String url;
    private String email;
    private String oldHtml;
    private String newHtml;
    private double thresholdPercent;
    private double preProcessedDiff;
    private double percentDiff;
    private double percentSimilarity;
    private Boolean emailNotSent;


    public WebPoller(String url, String email, String oldHtml, double thresholdPercent, int trackerNumber) {
        this.url = url;
        this.email = email;
        this.oldHtml = oldHtml;
        this.thresholdPercent = thresholdPercent;
        this.trackerNumber = trackerNumber;
        this.emailNotSent = true;
    }

    public void run() {
        try {
            newHtml = getHtml(url);
        } catch (Exception ex) {
            systemLogHtmlGetFailed(url);
            ex.printStackTrace();
        }
        percentSimilarity = compareStrings(oldHtml, newHtml) * 100;
        setPercentDiff(percentSimilarity);
        trackerTile = trackerTiles.get(trackerNumber);
        if (trackerTile.emailAlertsEnabled() && percentDiff >= thresholdPercent && emailNotSent) {
            sendMail(url, email, thresholdPercent);
            emailNotSent = false;
        }
        if (percentDiff < thresholdPercent) {
            emailNotSent = true;
        }

    }

    public double setPercentDiff(double percentSimilarity) {
        double d = (100.00 - percentSimilarity);
        System.out.println(d);
        System.out.println(percentSimilarity);
        preProcessedDiff = d;
        DecimalFormat df = new DecimalFormat("#.##");
        percentDiff = Double.parseDouble(df.format(d));
        System.out.println(percentDiff);
        return percentDiff;
    }

    public String getOldHtml() {
        return oldHtml;
    }

    public String getNewHtml() {
        return newHtml;
    }

    public Double getThresholdPercent() {
        return thresholdPercent;
    }

    public double getpreProcessedDiff() {
        return preProcessedDiff;
    }

    public double getPercentDiff() {
        return percentDiff;
    }
}
