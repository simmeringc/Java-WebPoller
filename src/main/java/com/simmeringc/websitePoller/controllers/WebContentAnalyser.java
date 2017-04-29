/**
 * Created by Conner on 4/29/17.
 */

package com.simmeringc.websitePoller.controllers;

import com.simmeringc.websitePoller.views.MainWindow;
import org.apache.commons.lang3.StringUtils;

public class WebContentAnalyser {

    public synchronized int getLevenshteinDistance(String url, String newHtml) {
        String oldHtml = MainWindow.cache.get();
        int levenshteinDistance = StringUtils.getLevenshteinDistance(url, newHtml);
    }
}
