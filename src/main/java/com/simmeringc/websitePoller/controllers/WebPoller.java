/**
 * Created by simmeringc on 4/28/17.
 *
 * master polling thread, calls StringSimilarity to
 * get website changes, sends emails, only sends 1 email
 * and newHtml does not replace oldHtml on threshold clear
 */

package com.simmeringc.websitePoller.controllers;

import com.simmeringc.websitePoller.structs.SimilarityResult;
import com.simmeringc.websitePoller.views.TrackerTile;

import static com.simmeringc.websitePoller.controllers.GoogleMail.sendMail;
import static com.simmeringc.websitePoller.controllers.StringSimilarity.compareStrings;
import static com.simmeringc.websitePoller.views.MainWindow.trackerTiles;
import static com.simmeringc.websitePoller.views.SystemLog.systemLogDiffDetected;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WebPoller implements Runnable {
    private TrackerTile trackerTile;
    private SimilarityResult similarityResult;
    private int trackerNumber;
    private String url;
    private String email;
    private String oldHtml;
    private String newHtml;
    private double thresholdPercent;
    private double formattedDiff;
    private double unformattedDiff;
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
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future getHtml = executor.submit(new WebRequester(url));
            newHtml = getHtml.get().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        similarityResult = compareStrings(oldHtml, newHtml);

        formattedDiff = similarityResult.getFormattedDifference();
        unformattedDiff = similarityResult.getUnformattedDifference();
        trackerTile = trackerTiles.get(trackerNumber);
        if (trackerTile.emailAlertsEnabled() && formattedDiff >= thresholdPercent && emailNotSent) {
            systemLogDiffDetected(url, thresholdPercent);
            sendMail(url, email, thresholdPercent);
            emailNotSent = false;
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

    public double getFormattedDiff() {
        return formattedDiff;
    }

    public double getUnformattedDiff() {
        return unformattedDiff;
    }
}
