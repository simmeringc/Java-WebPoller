/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.view;

import static com.simmeringc.websitePoller.view.SystemLog.*;

public class InputVerifier {

    public static void verifyInput(String urlFormText, String emailFormText, String changeThresholdFormText, String pollIntervalFormText) throws Exception {
        String urlFormat = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        String emailFormat = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        if (!urlFormText.matches(urlFormat)) {
            systemLogValidateURLInputFailed();
            throw new Exception("invalid URL");
        }

        if (!emailFormText.matches(emailFormat)) {
            systemLogValidateEmailInputFailed();
            throw new Exception("invalid email");
        }

        try {
            float thresholdInput = Float.parseFloat(changeThresholdFormText);
            if (thresholdInput < 0) {
                systemLogValidateChangeThresholdInputFailed();
                throw new Exception("threshold must be positive");
            }
        }
        catch (Exception ex) {
            systemLogValidateChangeThresholdInputFailed();
            throw new Exception("invalid change threshold");
        }

        try {
            float pollInterval = Float.parseFloat(pollIntervalFormText);
            if (pollInterval < 0) {
                systemLogValidatePollIntervalInputFailed();
                throw new Exception("interval must be positive");
            }
        }
        catch (Exception ex) {
            systemLogValidatePollIntervalInputFailed();
            throw new Exception("invalid poll interval");
        }
    }
}
