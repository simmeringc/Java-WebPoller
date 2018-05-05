/**
 * Created by simmeringc on 5/5/18.
 */

package com.simmeringc.websitePoller.structs;

public class SimilarityResult {

  private double similarity;
  private double unformattedDiff;
  private double formattedDiff;

  public SimilarityResult(double similarity, double unformattedDiff, double formattedDiff) {
    this.similarity = similarity;
    this.unformattedDiff = unformattedDiff;
    this.formattedDiff = formattedDiff;
  }

  public double getFormattedDifference() {
    return formattedDiff;
  }

  public void setFormattedDifference(double formattedDiff) {
    this.formattedDiff = formattedDiff;
  }

  public double getUnformattedDifference() {
    return unformattedDiff;
  }

  public void setUnformattedDifference(double formattedDiff) {
    this.unformattedDiff = unformattedDiff;
  }

  public double getSimilarity() {
    return similarity;
  }

  public void setSimilarity(double similarity) {
    this.similarity = similarity;
  }
}