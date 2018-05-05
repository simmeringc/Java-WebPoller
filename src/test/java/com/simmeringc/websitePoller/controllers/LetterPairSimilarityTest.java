/**
 * Created by simmeringc on 5/5/18.
 */

package com.simmeringc.websitePoller.controllers;

import com.simmeringc.websitePoller.structs.SimilarityResult;

import static com.simmeringc.websitePoller.controllers.StringSimilarity.compareStrings;
import static org.junit.Assert.assertEquals;

public class LetterPairSimilarityTest {

  @org.junit.Test
  public void equalStrings() throws Exception {

    String filteredHtml1 = "Example Domain    This domain is established to be used for illustrative examples in documents. You may use this domain in examples without prior coordination or asking for permission.    More information...";
    String filteredHtml2 = "Example Domain    This domain is established to be used for illustrative examples in documents. You may use this domain in examples without prior coordination or asking for permission.    More information...";

    SimilarityResult similarityResult = compareStrings(filteredHtml1, filteredHtml2);

    assertEquals(similarityResult.getSimilarity(), 100.0, 0);
  }

  @org.junit.Test
  public void halfDifference() throws Exception {

    String alteredHtml1 = "abcd";
    String alteredHtml2 = "ab";

    SimilarityResult similarityResult = compareStrings(alteredHtml1, alteredHtml2);

    assertEquals(similarityResult.getSimilarity(), 50.0, 0);
  }

  @org.junit.Test
  public void thirdDifference() throws Exception {

    String alteredHtml1 = "abcdef";
    String alteredHtml2 = "ab";

    SimilarityResult similarityResult = compareStrings(alteredHtml1, alteredHtml2);

    assertEquals(similarityResult.getSimilarity(), 33.33, 0);
  }

  @org.junit.Test
  public void wordVariance() throws Exception {

    String alteredHtml1 = "france";
    String alteredHtml2 = "french";

    SimilarityResult similarityResult = compareStrings(alteredHtml1, alteredHtml2);

    assertEquals(similarityResult.getSimilarity(), 40.00, 0);
  }

  @org.junit.Test
  public void unequalDifference() throws Exception {

    String alteredHtml1 = "abcdef";
    String alteredHtml2 = "abc";

    SimilarityResult similarityResult = compareStrings(alteredHtml1, alteredHtml2);

    assertEquals(similarityResult.getSimilarity(), 57.14, 0);
  }

  @org.junit.Test
  public void similarStrings() throws Exception {

    String alteredHtml1 = "america";
    String alteredHtml2 = "united stated of america";

    SimilarityResult similarityResult = compareStrings(alteredHtml1, alteredHtml2);

    assertEquals(similarityResult.getSimilarity(), 52.17, 0);
  }
}
