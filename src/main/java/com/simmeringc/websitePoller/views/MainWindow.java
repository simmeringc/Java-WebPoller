/**
 * Created by Conner on 4/28/17.
 */

package com.simmeringc.websitePoller.views;

import com.simmeringc.websitePoller.controllers.WebPoller;
import static com.simmeringc.websitePoller.views.InputVerifier.verifyInput;
import static com.simmeringc.websitePoller.controllers.WebRequester.getHtml;
import static com.simmeringc.websitePoller.views.SystemLog.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainWindow {

    //swing scope references
    private JFrame frame;
    private JLabel urlFormLabel, emailFormLabel, thresholdFormLabel, pollIntervalFormLabel;
    private JPanel inputPanel, logPanel, trackerPanel;
    private JTextField urlForm, emailForm, thresholdForm, pollIntervalForm;
    private JButton enterButton, helpButton;
    private JScrollPane systemLogPanel, trackerScrollContainer;
    private String oldHtml;

    //website counter
    private static int trackerNumber = 0;

    //keep track of trackers
    public static ArrayList<JPanel> trackerTileList = new ArrayList<JPanel>();
    public static ArrayList<WebPoller> webPollerList = new ArrayList<WebPoller>();

    //instantiate systemLog systemLogHelper
    private SystemLog systemLog = new SystemLog();

    public static void main(String[] args) {
        //take the menu bar off the JFrame
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        //set the name of the application menu item
        System.setProperty("apple.awt.application.name", "SnapLogic Website WebPoller");

        //build the GUI
        new MainWindow().buildGUI();
    }

    public void buildGUI() {

        //create frame - set frame parameters
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(809, 601);
        frame.setTitle("SnapLogic Website WebPoller");
        frame.setResizable(false);

        //create panel component containers
        inputPanel = new JPanel();
        logPanel = new JPanel();

        //trackerPanel holds trackerTiles inside the trackerScrollContainer panel
        trackerPanel = new JPanel();
        trackerPanel.setLayout(new BoxLayout(trackerPanel, BoxLayout.Y_AXIS));
        trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Tracking " + 0 + " websites"));
        trackerScrollContainer = new JScrollPane(trackerPanel);
        trackerScrollContainer.setPreferredSize(new Dimension(500, 400));
        trackerScrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        trackerScrollContainer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //component: URL input form
        urlFormLabel = new JLabel("URL:");
        urlForm = new JTextField(12);
        urlForm.addKeyListener(new FormListener());

        //component: email input form
        emailFormLabel = new JLabel("Email:");
        emailForm = new JTextField(12);
        emailForm.addKeyListener(new FormListener());

        //component: change-threshold input form
        thresholdFormLabel = new JLabel("Threshold (%):");
        thresholdForm = new JTextField(2);
        thresholdForm.addKeyListener(new FormListener());

        //component: poll-interval input form
        pollIntervalFormLabel = new JLabel("Interval (s):");
        pollIntervalForm = new JTextField(2);
        pollIntervalForm.addKeyListener(new FormListener());

        //component: enter button
        enterButton = new JButton("Enter");
        enterButton.addActionListener(new EnterButtonListener());

        //component: systemLogHelp button
        helpButton = new JButton("Help");
        helpButton.addActionListener(new SystemLogHelpButtonListener());

        //append components to inputPanel
        inputPanel.add(urlFormLabel);
        inputPanel.add(urlForm);
        inputPanel.add(emailFormLabel);
        inputPanel.add(emailForm);
        inputPanel.add(thresholdFormLabel);
        inputPanel.add(thresholdForm);
        inputPanel.add(pollIntervalFormLabel);
        inputPanel.add(pollIntervalForm);
        inputPanel.add(enterButton);
        inputPanel.add(helpButton);

        //component: scroll panel inheriting from systemLog
        systemLogPanel = new JScrollPane(systemLog.getSystemLogTextArea());

        //append components to logPanel - show tutorial
        logPanel.add(systemLogPanel);
        systemLogHelp();

        //tesing
        urlForm.setText("http://www.simmeringc.com");
        emailForm.setText("connersimmering@gmail.com");
        thresholdForm.setText("10");
        pollIntervalForm.setText("10");

        //append panels to frame
        frame.getContentPane().add(BorderLayout.SOUTH, inputPanel);
        frame.getContentPane().add(BorderLayout.CENTER, logPanel);
        frame.getContentPane().add(BorderLayout.NORTH, trackerScrollContainer);
        frame.setVisible(true);
    }

    public void reDraw() {
        frame.validate();
        frame.repaint();
    }

    public String getUrlFormText() {
        return urlForm.getText();
    }

    public String getEmailFormText() {
        return emailForm.getText();
    }

    public String getThresholdFormText() {
        return thresholdForm.getText();
    }

    public String getPollIntervalFormText() {
        return pollIntervalForm.getText();
    }

    //trackerScrollContainer counter redraw
    public void incrementTrackerPanelCounter() {
        trackerNumber++;
        trackerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY), "Websites being tracked: " + trackerNumber + " "));
    }

    //create new thread for intervaled content analysis
    public void createPoller() {
        double thresholdPercent = Double.parseDouble(getThresholdFormText());
        long interval = Long.parseLong(getPollIntervalFormText());

        WebPoller pollerRunnable = new WebPoller(getUrlFormText(), oldHtml, trackerNumber, thresholdPercent);
        webPollerList.add(pollerRunnable);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(pollerRunnable, 0, interval, TimeUnit.SECONDS);
    }

    //method: called from EnterButtonListener to create and append TrackerTile to TrackerPanel in GUI
    public void addTrackerTile() {
        createPoller();
        TrackerTile trackerTile = new TrackerTile(getUrlFormText(), getEmailFormText(), getThresholdFormText(), getPollIntervalFormText(), trackerNumber);
        JSeparator jSeparator = new JSeparator();
        jSeparator.setMaximumSize(new Dimension(5, 5));
        jSeparator.setMinimumSize(new Dimension(5, 5));
        trackerTileList.add(trackerTile);
        trackerPanel.add(trackerTile);
        trackerPanel.add(jSeparator);
        incrementTrackerPanelCounter();
        reDraw();
    }

    //inner-class: form listener on form fields allows user to hit the enter key to send input to the application
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

    //inner-class: primary call to action
    class EnterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String urlFromText = getUrlFormText();
            String url = urlFromText;
            String emailFormText = getEmailFormText();
            String changeThresholdText = getThresholdFormText();
            String pollIntervalText = getPollIntervalFormText();

            try {
                verifyInput(urlFromText, emailFormText, changeThresholdText, pollIntervalText);
                try {
                    oldHtml = getHtml(url);
                    addTrackerTile();
                    systemLogHtmlGetSuccessful();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //inner-class: displays help intro message
    class SystemLogHelpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            systemLogHelp();
        }
    }
}



