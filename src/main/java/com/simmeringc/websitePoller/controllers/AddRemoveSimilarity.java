/**
 * Created by Conner on 4/29/17.
 *
 * computes the character pairs from the words of each of the two input strings,
 * then iterates through the ArrayLists to find the size of the intersection,
 * calls HtmlSanitizer to filter strings
 */

package com.simmeringc.websitePoller.controllers;
import static com.simmeringc.websitePoller.controllers.HtmlSanitizer.defaultPolicy;

import java.util.ArrayList;

public class AddRemoveSimilarity {

    //String -> ArrayList<char>
    private static ArrayList charList(String str) {
        ArrayList allChars = new ArrayList();
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            allChars.add(charArray[i]);
        }
        return allChars;
    }

    //Return lexical similarity value in the range [0,1]
    public synchronized static double compareStrings(String str1, String str2) {
        ArrayList chars1 = charList(str1.toUpperCase());
        ArrayList chars2 = charList(str2.toUpperCase());
        double intersection = 0;
        double longestString = Math.max(chars1.size(), chars2.size());
        for (int i=0; i<chars1.size(); i++) {
            Object pair1=chars1.get(i);
            for(int j=0; j<chars2.size(); j++) {
                Object pair2=chars2.get(j);
                    if (pair1.equals(pair2)) {
                        intersection++;
                        chars2.remove(j);
                        break;
                    }
                }
            }
        return (intersection/longestString);
    }

    public static void main(String[] args) {
        System.out.println(compareStrings("ABCD", "ABCD"));
        System.out.println(compareStrings("ABCD", "DB"));
        System.out.println(compareStrings("ABCD" , "ABCDE"));
        System.out.println(compareStrings("ABCD" , "ACD"));
    }
}
