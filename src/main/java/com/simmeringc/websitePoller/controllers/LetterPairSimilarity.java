/**
 * Created by Conner on 4/29/17.
 *
 * computes the character pairs from the words of each of the two input strings,
 * then iterates through the ArrayLists to find the size of the intersection.
 */

package com.simmeringc.websitePoller.controllers;

import static com.simmeringc.websitePoller.controllers.HtmlSanitizer.defaultPolicy;

import java.util.ArrayList;

public class LetterPairSimilarity {

    //return an array of adjacent char pairs contained in the input string | input="snap" output=[sn,na,ap]
    private static ArrayList letterPairs(String str) {
        int numPairs = str.length()-1;
        ArrayList pairs = new ArrayList();
        for (int i=0; i<numPairs; i++) {
            pairs.add(str.substring(i,i+2));
        }
        return pairs;
    }

    //return an ArrayList of 2-character strings
    private static ArrayList wordLetterPairs(String str) {
        ArrayList allPairs = new ArrayList();
        //string cleaning - sanitize with policy, remove whitespace and newlines
        String filteredString = defaultPolicy(str);
        filteredString = filteredString.replaceAll("\\s+","");
        filteredString = filteredString.replaceAll("\\n+","");
        //tokenize the string and put the tokens/words into an array
        String[] words = filteredString.split("\\s");
        //for each word
        for (int w=0; w < words.length; w++) {
            //find the pairs of characters
            ArrayList pairsInWord = letterPairs(words[w]);
            for (int p=0; p < pairsInWord.size(); p++) {
                allPairs.add(pairsInWord.get(p));
            }
        }
        return allPairs;
    }

    //return lexical similarity value in the range [0,1]
    public synchronized static double compareStrings(String str1, String str2) {
        ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
        ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();
        for (int i=0; i<pairs1.size(); i++) {
            Object pair1=pairs1.get(i);
            for(int j=0; j<pairs2.size(); j++) {
                Object pair2=pairs2.get(j);
                    if (pair1.equals(pair2)) {
                        intersection++;
                        //remove character pairs from the 2nd arrayList so "GGGGG" doesn't score perfectly with "GG"
                        pairs2.remove(j);
                        break;
                    }
                }
            }
        return (2.0*intersection)/union;
    }
}
