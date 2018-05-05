/**
 * Created by simmeringc on 4/29/17.
 *
 * This string similarity algorithm is designed to provide a consistent reflection of lexical similarity
 * and a robustness to changes of word order, where strings with small differences should
 * be recognised as being similar and two strings which contain the same words but in a different order
 * should be recognized as being similar.
 *
 * Computes adjacent character pairs contained between two strings
 * then iterates through the lists to find the percent difference between these strings.
 *
 * Much faster than a levenshtein distance calculation but not as thoroughly tested
 * and not as precise with smaller strings, luckily, HTML page strings tend to be large.
 */

package com.simmeringc.websitePoller.controllers;

import com.simmeringc.websitePoller.structs.SimilarityResult;

import static com.simmeringc.websitePoller.controllers.HtmlSanitizer.defaultPolicy;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StringSimilarity {

    //return an array of adjacent char pairs contained in the input string | input="snap" output=[sn,na,ap]
    public static ArrayList wordPairs(String str) {
        ArrayList wordPairs = new ArrayList();
        for (int i = 0; i < str.length()-1; i++) {
            wordPairs.add(str.substring(i, i+2));
        }
        return wordPairs;
    }

    //return an ArrayList of 2-character strings
    public static ArrayList stringPairs(String str) {
        ArrayList stringPairs = new ArrayList();
        String filteredString = defaultPolicy(str);
        //tokenize the string and put the tokens into an array
        String[] words = filteredString.split("\\s");
        //for each word
        for (int w = 0; w < words.length; w++) {
            //find the pairs of characters
            ArrayList pairsInWord = wordPairs(words[w]);
            System.out.println("word: " + w + ": " + words[w] + " " + pairsInWord);
            for (int p = 0; p < pairsInWord.size(); p++) {
                stringPairs.add(pairsInWord.get(p));
            }
        }
        return stringPairs;
    }

    /**
     * Return the lexical similarity in the range [0,1]
     *
     * Out of all letter pairs from str1 and str2
     * how many of these pairs are the same?
     *
     * Return the percent similarity by preforming
     * ((numberOfSets * setIntersections) / unionOfSets) * 100,
     * where the unionOfSets is the total size of all pairs
     */
    public synchronized static SimilarityResult compareStrings(String str1, String str2) {
        System.out.println("str1: " + str1);
        System.out.println("str2: " + str2);
        ArrayList pairs1 = stringPairs(str1.toUpperCase());
        ArrayList pairs2 = stringPairs(str2.toUpperCase());
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();
        for (int i = 0; i < pairs1.size(); i++) {
            Object pair1 = pairs1.get(i);
            for (int j = 0; j < pairs2.size(); j++) {
                Object pair2 = pairs2.get(j);
                    if (pair1.equals(pair2)) {
                        intersection++;
                        //don't count duplicate intersections
                        pairs2.remove(j);
                        break;
                    }
                }
            }
        double unformattedSimilarity = ((2.0 * intersection/union) * 100);
        double unformattedDiff = (100.00 - unformattedSimilarity);
        DecimalFormat df = new DecimalFormat("#.##");
        double formattedSimilarity = Double.parseDouble(df.format(unformattedSimilarity));
        double formattedDiff = Double.parseDouble(df.format(unformattedDiff));

        System.out.println("intersection: " + intersection + " (*2 = " + intersection*2 + ")");
        System.out.println("union: " + union);
        System.out.println("similarity: " + formattedSimilarity + "%");
        System.out.println("difference: " + formattedDiff + "%");
        System.out.println("");

        return new SimilarityResult(formattedSimilarity, unformattedDiff, formattedDiff);
    }
}
