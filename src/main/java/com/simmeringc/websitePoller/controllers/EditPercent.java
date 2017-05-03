/**
 * Created by Conner on 4/29/17.
 *
 * Levenshtein edit distance similarity calculation
 */

package com.simmeringc.websitePoller.controllers;

import static org.apache.commons.lang3.StringUtils.getLevenshteinDistance;

class EditPercent {

    //calculates the similarity (a number within 0 and 1) between two strings.
    public static double getSimilarity(String s1, String s2) {
        double longerString = Math.max(s1.length(), s2.length());
        System.out.println("starting");
        System.out.println("math: " + longerString + " - " + getLevenshteinDistance(s1, s2) + " / " + longerString);
        if (longerString == 0) { return 1.0; }
        return ((longerString - getLevenshteinDistance(s1, s2)) / longerString);
    }

//    public static void printSimilarity(String s1, String s2) {
//        System.out.println(s1 + " " + s2 + " are " + (1 - (similarity(s1, s2))) + "% different");
//    }
//
//    public static void main(String[] args) {
//        printSimilarity("ABCD", "DB");
//        printSimilarity("ABCD", "ABCDE");
//        printSimilarity("ABCD", "ACD");
//    }

}