/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.controllers;

import com.simmeringc.websitePoller.views.TrackerTile;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

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
    private double preProssesedDiff;
    private double percentDiff;
    private double percentSimilarity;


    public WebPoller(String url, String email, String oldHtml, double thresholdPercent, int trackerNumber) {
        this.url = url;
        this.email = email;
        this.oldHtml = oldHtml;
        this.thresholdPercent = thresholdPercent;
        this.trackerNumber = trackerNumber;
    }

    public void run() {
        try {
            newHtml = getHtml(url);
        } catch (Exception ex) {
            systemLogHtmlGetFailed(url);
            ex.printStackTrace();
        }
        percentSimilarity = compareStrings(oldHtml, newHtml);
        setPercentDiff(percentSimilarity);
        trackerTile = trackerTiles.get(trackerNumber);
        if (trackerTile.emailAlertsEnabled() && percentDiff >= thresholdPercent) {
            System.out.println("attempting send");
            sendMail(url, email, thresholdPercent);
        }

    }

    public double setPercentDiff(double percentSimilarity) {
        if (percentSimilarity == 1.0) {
            return 0.00;
        } else {
            double d = (1.00 - percentSimilarity);
            preProssesedDiff = d;
            DecimalFormat df = new DecimalFormat("#.##");
            percentDiff = Double.parseDouble(df.format(d));
            return percentDiff;
        }
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

    public double getPreProssesedDiff() {
        return preProssesedDiff;
    }

    public double getPercentDiff() {
        return percentDiff;
    }

    public double getPercentSimilarity() {
        return percentSimilarity;
    }
}
