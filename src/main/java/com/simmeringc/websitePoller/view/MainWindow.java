/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.view;

import com.simmeringc.websitePoller.model.Cache;
import com.simmeringc.websitePoller.model.Node;
import static com.simmeringc.websitePoller.view.InputVerifier.verifyInput;
import static com.simmeringc.websitePoller.controller.WebRequester.getHTML;
import static com.simmeringc.websitePoller.view.SystemLog.systemLogHelp;
import static com.simmeringc.websitePoller.view.SystemLog.systemLogHTMLGetSuccessful;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainWindow {

    //swing references
    JFrame frame;
    JLabel urlFormLabel, emailFormLabel, changeThresholdFormLabel, pollIntervalFormLabel;
    JPanel inputPanel, logPanel, trackerPanel;
    JTextField urlForm, emailForm, changeThresholdForm, pollIntervalForm;
    JButton enterButton, systemLogHelpButton;
    JScrollPane scroll;

    //instantiate systemLog systemLogHelper
    SystemLog systemLog = new SystemLog();

    //instantiate cache model
    Cache cache = new Cache();

    public static void main(String[] args) {
        //take the menu bar off the JFrame
        System.setProperty("apple.laf.useScreenMenuBar", "false");

        //set the name of the application menu item
        System.setProperty("apple.awt.application.name", "SnapLogic Website Poller");

        //build the GUI
        new MainWindow().buildGUI();
    }

    public void buildGUI() {

        //create frame - set frame parameters
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(809, 601);
        frame.setTitle("SnapLogic Website Poller");
        frame.setResizable(false);

        //create panel component containers
        inputPanel = new JPanel();
        logPanel = new JPanel();
        trackerPanel = new JPanel();

        //component: URL input form
        urlFormLabel = new JLabel("URL:");
        urlForm = new JTextField(12);
        urlForm.addKeyListener(new FormListener());

        //component: email input form
        emailFormLabel = new JLabel("Email:");
        emailForm = new JTextField(12);
        emailForm.addKeyListener(new FormListener());

        //component: change-threshold input form
        changeThresholdFormLabel = new JLabel("Threshold (%):");
        changeThresholdForm = new JTextField(2);
        changeThresholdForm.addKeyListener(new FormListener());

        //component: poll-interval input form
        pollIntervalFormLabel = new JLabel("Interval (s):");
        pollIntervalForm = new JTextField(2);
        pollIntervalForm.addKeyListener(new FormListener());

        //component: enter button
        enterButton = new JButton("Enter");
        enterButton.addActionListener(new EnterButtonListener());

        //component: systemLogHelp button
        systemLogHelpButton = new JButton("systemLogHelp");
        systemLogHelpButton.addActionListener(new systemLogHelpButtonListener());

        //append components to inputPanel
        inputPanel.add(urlFormLabel);
        inputPanel.add(urlForm);
        inputPanel.add(emailFormLabel);
        inputPanel.add(emailForm);
        inputPanel.add(changeThresholdFormLabel);
        inputPanel.add(changeThresholdForm);
        inputPanel.add(pollIntervalFormLabel);
        inputPanel.add(pollIntervalForm);
        inputPanel.add(enterButton);
        inputPanel.add(systemLogHelpButton);

        //component: text-scroll box inheriting from systemLog
        scroll = new JScrollPane(systemLog.getSystemLogTextArea());

        //append components to logPanel - show tutorial
        logPanel.add(scroll);
        systemLogHelp();

        //trackerPanel parameters
        trackerPanel.setPreferredSize(new Dimension(0, 400));
        trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Websites being tracked: "));


        //append panels to frame - swing border layout
        frame.getContentPane().add(BorderLayout.SOUTH, inputPanel);
        frame.getContentPane().add(BorderLayout.CENTER, logPanel);
        frame.getContentPane().add(BorderLayout.NORTH, trackerPanel);
        frame.setVisible(true);
    }

    public void addTracker() {
        
    }

    //inner class form listener on form fields allows user to hit the enter key to send input to the application
    class FormListener implements KeyListener {
        public void keyPressed(KeyEvent event) {
            if (event.getKeyCode()==KeyEvent.VK_ENTER){
                enterButton.doClick();
            }
        }
        public void keyReleased(KeyEvent event) {
            //abstract method stub
        }
        public void keyTyped(KeyEvent event) {
            //abstract method stub
        }
    }

    //inner class primary call to action
    class EnterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String urlFromText = urlForm.getText();
            String url = urlFromText;
            String emailFormText = emailForm.getText();
            String changeThresholdText = changeThresholdForm.getText();
            String pollIntervalText = pollIntervalForm.getText();
            String html = null;

            try {
                verifyInput(urlFromText, emailFormText, changeThresholdText, pollIntervalText);
                try {
                    html = getHTML(url);
                    systemLogHTMLGetSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Node page = new Node(html);
            cache.put(url, page);
        }
    }

    //inner class displays help intro message
    class systemLogHelpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            systemLogHelp();
        }
    }
}



