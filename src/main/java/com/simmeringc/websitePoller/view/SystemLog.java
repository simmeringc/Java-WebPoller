/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.view;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemLog {

    private static JTextArea systemLogTextArea;

    public SystemLog() {
        systemLogTextArea = new JTextArea(8, 66);
        systemLogTextArea.setLineWrap(true);
        systemLogTextArea.setText(new Date() + " System log:");
        DefaultCaret caret = (DefaultCaret) systemLogTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    public JTextArea getSystemLogTextArea() {
        return systemLogTextArea;
    }

    public static String timeStamp() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }
    public static void systemLogHelp() {
        String line1 = "1. SnapLogic Website Poller: get email alerts when the content on a webpage changes";
        String line2 = "2. Enter a URL and an email to be alerted upon a webpage change.";
        String line3 = "3. Set the change-threshold (percent) and poll-interval (seconds), then hit 'Enter'.";
        String line4 = "4. If the threshold is set to 10, an email will be sent when 10% of the wep page changes, etc.";
        String line5 = "5. The SnapLogic Website Poller will continue to poll websites every interval and send alerts until the app is closed.";
        systemLogTextArea.setText(systemLogTextArea.getText() + "\n" + "\n" + line1 + "\n" + line2 + "\n" + line3 + "\n" + line4 + "\n" + line5);
    }
    public static void systemLogHTMLGetSuccessful() {
        String newMessage = timeStamp() + " -" + " HTML get() successful.";
        systemLogTextArea.setText(systemLogTextArea.getText() + "\n" + newMessage);
    }
    public static void systemLogHTMLGetFailed() {
        String newMessage = timeStamp() + " -" + " HTML get() failed.";
        systemLogTextArea.setText(systemLogTextArea.getText() + "\n" + newMessage);
    }
    public static void systemLogValidateURLInputFailed() {
        String newMessage = timeStamp() + " -" + " Invalid URL, please use the full URL from your browser's address bar.";
        systemLogTextArea.setText(systemLogTextArea.getText() + "\n" + newMessage);
    }
    public static void systemLogValidateEmailInputFailed() {
        String newMessage = timeStamp() + " -" + " Invalid email, please enter a valid email.";
        systemLogTextArea.setText(systemLogTextArea.getText() + "\n" + newMessage);
    }
    public static void systemLogValidateChangeThresholdInputFailed() {
        String newMessage = timeStamp() + " -" + " Invalid threshold interval, please enter a positive integer or float.";
        systemLogTextArea.setText(systemLogTextArea.getText() + "\n" + newMessage);
    }
    public static void systemLogValidatePollIntervalInputFailed() {
        String newMessage = timeStamp() + " -" + " Invalid poll interval, please enter a positive integer or float.";
        systemLogTextArea.setText(systemLogTextArea.getText() + "\n" + newMessage);
    }
}

