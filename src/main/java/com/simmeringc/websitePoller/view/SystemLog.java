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
        systemLogTextArea = new JTextArea(8, 65);
        systemLogTextArea.setLineWrap(true);
        systemLogTextArea.setText(new Date() + " System log:");
        DefaultCaret caret = (DefaultCaret) systemLogTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    public JTextArea getSystemLogTextArea() {
        return systemLogTextArea;
    }

    public String timeStamp() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }
    public void help() {
        String line1 = "1. SnapLogic Website Poller: get email alerts when a web page changes based on a custom change-threshold";
        String line2 = "2. Enter a URL and an email to be alerted upon web page change. Set the change-threshold and hit 'Enter'.";
        String line3 = "3. If the threshold is set to 10%, an email will be sent when 10% of the wep page changes, etc.";
        String line4 = "4. The SnapLogic Website Poller will continue to poll websites for changes until the app is closed.";
        systemLogTextArea.setText(systemLogTextArea.getText() + "\n" + "\n" + line1 + "\n" + line2 + "\n" + line3 + "\n" + line4);
    }
    public void htmlGetSuccessful() {
        String newMessage = timeStamp() + " HTML get() successful";
        systemLogTextArea.setText(systemLogTextArea.getText() + "\n" + newMessage);
    }
}

