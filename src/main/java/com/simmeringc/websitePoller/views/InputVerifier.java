/**
 * Created by simmeringc on 4/28/17.
 *
 * verifies MainWindow form input
 */

package com.simmeringc.websitePoller.views;

import static com.simmeringc.websitePoller.views.SystemLog.*;

public class InputVerifier {

    public static void verifyInput(String urlFormText, String emailFormText, String changeThresholdFormText, String pollIntervalFormText) throws Exception {
        String urlFormat = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        String emailFormat = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        if (!urlFormText.matches(urlFormat)) {
            systemLogValidateUrlInputFailed();
            throw new Exception("invalid URL");
        }

        if (!emailFormText.matches(emailFormat)) {
            systemLogValidateEmailInputFailed();
            throw new Exception("invalid email");
        }

        try {
            int thresholdInput = Integer.parseInt(changeThresholdFormText);
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
            int pollInterval = Integer.parseInt(pollIntervalFormText);
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
